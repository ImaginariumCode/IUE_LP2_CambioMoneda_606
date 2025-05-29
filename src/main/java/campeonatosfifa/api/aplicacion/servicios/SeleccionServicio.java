package campeonatosfifa.api.aplicacion.servicios;

import java.util.List;

import org.springframework.stereotype.Service;

import campeonatosfifa.api.core.dominio.entidades.Seleccion;
import campeonatosfifa.api.core.interfaces.servicios.ISeleccionServicio;
import campeonatosfifa.api.infraestructura.repositorios.ISeleccionRepositorio;

@Service
public class SeleccionServicio implements ISeleccionServicio {

    private ISeleccionRepositorio repositorio;

    public SeleccionServicio(ISeleccionRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Seleccion> listar() {
        return repositorio.findAll();
    }

    @Override
    public Seleccion obtener(int id) {
        return repositorio.findById(id).orElse(null);
    }

    @Override
    public List<Seleccion> buscar(String dato) {
        return repositorio.buscar(dato);
    }

    @Override
    public Seleccion agregar(Seleccion seleccion) {
        return repositorio.save(seleccion);
    }

    @Override
    public Seleccion modificar(Seleccion seleccion) {
        if (repositorio.existsById(seleccion.getId())) {
            return repositorio.save(seleccion);
        }
        return null;
    }

    @Override
    public boolean eliminar(int id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return true;
        }
        return false;
    }
} 