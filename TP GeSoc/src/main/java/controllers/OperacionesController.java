package controllers;

import repositorios.RepositorioCompras;
import repositorios.RepositorioOrganizaciones;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class OperacionesController {


    public ModelAndView getOperaciones(Request request, Response response) {
        Map<String, Object> modelo = new HashMap<>();
        //Obtener del repo las operaciones relacionadas a la entidad actual
        modelo.put("operaciones", RepositorioCompras.instance().obtenerOperaciones("entidad = " + request.params(":idEntidad")));

        return new ModelAndView(modelo, "operaciones.html.hbs");
    }

    public ModelAndView getFormOperaciones(){

        return new ModelAndView(null,"crearOperaciones.html.hbs");
    }

    public ModelAndView getOperacion(Request request, Response response) {
        return null;
    }
}
