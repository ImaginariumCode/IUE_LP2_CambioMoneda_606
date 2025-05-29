package campeonatosfifa.api.presentacion.controladores;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import campeonatosfifa.api.core.dominio.entidades.Seleccion;
import campeonatosfifa.api.core.interfaces.servicios.ISeleccionServicio;

@RestController
@RequestMapping("/api/selecciones")
public class SeleccionControlador {

    private ISeleccionServicio servicio;

    public SeleccionControlador(ISeleccionServicio servicio) {
        this.servicio = servicio;
    }

    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public List<Seleccion> listar() {
        return servicio.listar();
    }

    @GetMapping("/obtener/{id}")
    public Seleccion obtener(@PathVariable int id) {
        return servicio.obtener(id);
    }

    @GetMapping("/buscar/{dato}")
    public List<Seleccion> buscar(@PathVariable String dato) {
        return servicio.buscar(dato);
    }

    @PostMapping("/agregar")
    public Seleccion agregar(@RequestBody Seleccion seleccion) {
        return servicio.agregar(seleccion);
    }

    @PutMapping("/modificar")
    public Seleccion modificar(@RequestBody Seleccion seleccion) {
        return servicio.modificar(seleccion);
    }

    @DeleteMapping("/eliminar/{id}")
    public boolean eliminar(@PathVariable int id) {
        return servicio.eliminar(id);
    }
} 