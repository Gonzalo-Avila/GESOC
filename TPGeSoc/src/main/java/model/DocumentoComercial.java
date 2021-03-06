package model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/*@Entity
@Table(name = "documentos_comerciales")*/
@Embeddable
public class DocumentoComercial {
	
	/*@Id
	@GeneratedValue*/
	@Column(name = "id_documento_comercial")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_documento")
	private TipoDocumentoComercial tipoDoc;
	
	public DocumentoComercial(Long id, TipoDocumentoComercial tipoDoc){
		this.id = id;
		this.tipoDoc=tipoDoc;
	}
	public DocumentoComercial(TipoDocumentoComercial tipoDoc){
		this.tipoDoc=tipoDoc;
	}
	
	public DocumentoComercial(){
		
	}

	public Long getId() {
		return id;
	}

	public TipoDocumentoComercial getTipoDoc() {
		return tipoDoc;
	}
}
