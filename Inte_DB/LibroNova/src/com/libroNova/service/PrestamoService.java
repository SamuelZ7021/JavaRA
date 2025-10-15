package com.libroNova.service;

import com.libroNova.dao.BookDaoImpl;
import com.libroNova.dao.LoanDaoImpl;
import com.libroNova.dao.PartnerDaoImpl;
import com.libroNova.exception.BusinessException;
import com.libroNova.exception.DataAccessException;
import com.libroNova.interfas.PrestamoServiceInterface;
import com.libroNova.model.Book;
import com.libroNova.model.Partner;
import com.libroNova.model.Prestamo;
import com.libroNova.util.DatabaseConnector;
import com.libroNova.util.LoggerUtil;
import com.libroNova.util.PropertiesLoader;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class PrestamoService implements PrestamoServiceInterface {

    private final LoanDaoImpl prestamoDAO;
    private final BookDaoImpl libroDao;
    private final PartnerDaoImpl partnerDAO;

    public PrestamoService() {
        this.prestamoDAO = new LoanDaoImpl();
        this.libroDao = new BookDaoImpl();
        this.partnerDAO = new PartnerDaoImpl();
    }

    @Override
    public Prestamo registrarPrestamo(int idLibro, int idSocio, int idUsuario) throws BusinessException {
        Optional<Book> libroOpt = libroDao.searchById(idLibro);
        if (libroOpt.isEmpty()) {
            throw new BusinessException("El libro con ID " + idLibro + " no existe.");
        }
        Book book = libroOpt.get();
        if (book.getStockDisponible() <= 0) {
            throw new BusinessException("No hay stock disponible para el libro '" + book.getTitulo() + "'.");
        }

        Optional<Partner> partnerOpt = partnerDAO.searchById(idSocio);
        if (partnerOpt.isEmpty()) {
            throw new BusinessException("El socio con ID " + idSocio + " no existe.");
        }
        Partner partner = partnerOpt.get();
        if (!partner.getEstado().equalsIgnoreCase("activo")) {
            throw new BusinessException("El socio '" + partner.getNombre() + "' no está activo.");
        }


        Connection conn = null;
        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);
            book.setStockDisponible(book.getStockDisponible() - 1);
            libroDao.updateWithConnection(book, conn);

            long diasDePrestamo = 1;
            try {
                diasDePrestamo = Long.parseLong(PropertiesLoader.getProperty("prestamo.dias.duracion"));
            } catch (NumberFormatException e) {
                LoggerUtil.logWarning("La propiedad 'prestamo.dias.duracion' no es un número válido. Usando valor por defecto de 15 días.");
            }
            LocalDateTime fechaDevolucion = LocalDateTime.now().plusDays(diasDePrestamo);
            Prestamo nuevoPrestamo = new Prestamo(idLibro, idSocio, idUsuario, fechaDevolucion, "activo");

            Prestamo prestamoCreado = prestamoDAO.createWithConnection(nuevoPrestamo, conn);

            conn.commit();
            LoggerUtil.logInfo("Préstamo registrado exitosamente: ID " + prestamoCreado.getId());
            return prestamoCreado;

        } catch (SQLException | DataAccessException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DataAccessException("Error crítico al intentar revertir la transacción.", ex);
                }
            }
            throw new BusinessException("Error al registrar el préstamo. La operación ha sido cancelada." + e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Prestamo registrarDevolucion(int idPrestamo) throws BusinessException {
        Optional<Prestamo> prestamoOpt = prestamoDAO.findById(idPrestamo);
        if(prestamoOpt.isEmpty()){
            throw new BusinessException("El préstamo con ID " + idPrestamo + " no existe.");
        }
        Prestamo prestamo = prestamoOpt.get();
        if(!prestamo.getEstado().equalsIgnoreCase("activo")){
            throw new BusinessException("Este préstamo ya fue devuelto o está en otro estado.");
        }

        Optional<Book> libroOpt = libroDao.searchById(prestamo.getIdLibro());
        if(libroOpt.isEmpty()){
            throw new BusinessException("El libro asociado a este préstamo ya no existe. Contacte a un administrador.");
        }
        Book book = libroOpt.get();

        Connection conn = null;
        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);

            book.setStockDisponible(book.getStockDisponible() + 1);
            libroDao.updateWithConnection(book, conn);

            prestamo.setEstado("devuelto");
            prestamo.setFechaDevolucionReal(LocalDateTime.now());
            prestamoDAO.updateWithConnection(prestamo, conn);

            conn.commit();
            return prestamo;
        } catch (SQLException | DataAccessException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DataAccessException("Error crítico al intentar revertir la transacción de devolución.", ex);
                }
            }
            throw new BusinessException("Error al registrar la devolución. La operación ha sido cancelada." + e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<Prestamo> obtenerTodosLosPrestamos() {
        return prestamoDAO.findAll();
    }

    @Override
    public List<Prestamo> obtenerPrestamosVencidos() {
        return prestamoDAO.findOverdueLoans();
    }
}
