package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import exceptions.*;
import organizacion.Organizacion;
import paymentMethods.IDMedioDePago;
import repositorios.RepositorioCompras;
import usuarios.*;
import validacionesOperaciones.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "operaciones_de_egreso")
public class OperacionDeEgreso {
	
	@Id
	@GeneratedValue
	@Column(name = "id_operacion")
	private Long id;


	@OneToMany(cascade = {CascadeType.ALL})
	@JoinColumn(name = "operacion_asociada")
	private List<Item> items = new ArrayList<>();
	
	/*@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "documento_comercial", referencedColumnName = "id_documento_comercial")*/
	@Embedded
	private DocumentoComercial documentoComercial;
	
	@Column(name = "fecha_operacion")
	private LocalDateTime fechaOperacion;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "medio_de_pago")
	private IDMedioDePago medio;

	@ManyToOne
	@JoinColumn(name = "proveedor")
	private Proveedor proveedor;
	
	@OneToMany(cascade = {CascadeType.ALL})
	@JoinColumn(name = "operacion_asociada")
	public List<Presupuesto> presupuestos = new ArrayList<>();
	
	@OneToOne(cascade = {CascadeType.ALL})
	@Column(name = "presupuesto_elegido")
	public Presupuesto presupuestoElegido;
	
	@ManyToMany
	@JoinTable(name = "revisor_operacion",
			joinColumns=
            @JoinColumn(name="id_operacion", referencedColumnName="id_operacion"),
            inverseJoinColumns=
            @JoinColumn(name="id_revisor", referencedColumnName="id_usuario")
    )
	private List<Usuario> revisores = new ArrayList<>();
	
	@OneToMany(cascade = {CascadeType.ALL})
	@Column(name = "validaciones_asociadas")
	private List<ValidacionDeOperaciones> validacionesVigentes = new ArrayList<>();
	
	@ElementCollection
	@CollectionTable(name = "etiquetas_operaciones",
					 joinColumns=@JoinColumn(name = "id_operacion"))
	public List<EtiquetaOperacion> etiquetas = new ArrayList<>();
	
	@Column(name = "presupuestos_minimos")
	public final int presupuestosMinimos = 1;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "estado_operacion")
	private EstadoOperacion estado = EstadoOperacion.PENDIENTE;
	
	public EstadoOperacion getEstado() {
		return estado;
	}
	
	public Long getId() {
		return id;
	}

	public void setEstado(EstadoOperacion estado) {
		this.estado = estado;
	}

	public OperacionDeEgreso() {}
	
	public OperacionDeEgreso(List<Item> items, List<ValidacionDeOperaciones> validaciones) {
		this.items = items;
		validacionesVigentes=validaciones;
	}
	
	public OperacionDeEgreso(List<Item> items, DocumentoComercial documentoComercial, LocalDateTime fechaOperacion, 
			IDMedioDePago medio, Proveedor proveedor, List<Presupuesto> presupuestos,
			List<Usuario> revisores,List<ValidacionDeOperaciones> validaciones) {
		
		this.items = items;
		this.documentoComercial = documentoComercial;
		this.fechaOperacion = fechaOperacion;
		this.medio = medio;
		this.proveedor = proveedor;
		this.revisores = revisores;
		//this.estado = EstadoOperacion.PENDIENTE;
		
		validacionesVigentes=validaciones;
	}
		
	public void setDocumentoComercial(DocumentoComercial codDocumentoComercial) {
		this.documentoComercial = codDocumentoComercial;
	}
	
	public void agregarEtiqueta(EtiquetaOperacion etiqueta) {
		etiquetas.add(etiqueta);
	}
	
	public void agregarNuevoPresupuesto(Presupuesto presupuesto) {

		if(!this.contieneTodos(presupuesto.getItems())) {
			throw new PresupuestoInvalidoException();
		}
		
		this.presupuestos.add(presupuesto);
	}
	//Así siempre se agregan presupuestos validos. Deberia ser la unica forma de agregar presupuestos.
	//Así respetamos el punto 2 de la entrega 2.
	
	public void setPresupuestoElegido(Presupuesto presupuesto) {
		this.presupuestoElegido = presupuesto;
	}
	
	public void agregarRevisor(Usuario revisorNuevo) {
		this.revisores.add(revisorNuevo);
	}

	public BigDecimal valorTotal() {
		return this.items.stream().map(item -> item.getMoneda().monto).reduce(BigDecimal.ZERO,BigDecimal::add);
	}
	
	public boolean contiene(Item item) {
		return this.items.stream().anyMatch(i -> i.equals(item));
	}

	private boolean contieneTodos(List<Item> items) {
		return items.stream().allMatch(item -> this.contiene(item));
	}
	
	public boolean contieneTodosLosItemsDelPresupuesto() {
		return presupuestoElegido.getItems().stream().allMatch(item -> this.contiene(item));
	}
	
	public BigDecimal menorPrecioDePresupuestos() {
		return this.presupuestos.stream().map(presupuesto -> presupuesto.valorTotal()).min(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
	}
	
	public BigDecimal valorTotalDelPresupuestoElegido() {
		return presupuestoElegido.valorTotal();
	}
	
	public int cantidadDePresupuestos() {
		return this.presupuestos.size();
	}
	
	public boolean esValida() {
		return validacionesVigentes.stream().allMatch(validacion -> validacion.pasaLaValidacion(this));
	}
	
	public void notificarRevisores(String mensaje) {
		this.revisores.forEach(revisor -> revisor.recibirMensaje(new Mensaje(mensaje + ", " + this.id))); 
	}
}
