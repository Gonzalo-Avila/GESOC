package validacionesOperaciones;

import java.math.BigDecimal;
import java.util.Comparator;

import model.OperacionDeEgreso;

public class ValidarQueSeHayaElegidoElPresupuestoMasBarato implements ValidadorDeOperaciones{
	
	private BigDecimal menorPrecioDePresupuestos(OperacionDeEgreso operacion) {
		return operacion.presupuestos.stream().map(presupuesto -> presupuesto.valorTotal()).min(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
	}
	public boolean pasoCorrectamente(OperacionDeEgreso operacion) {
		return this.menorPrecioDePresupuestos(operacion).equals(operacion.valorTotal());
	}

}