package model;

import javax.persistence.*;

@Entity
public class DocumentoComercial {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private TipoDocumentoComercial tipoDoc;
	
	public DocumentoComercial(TipoDocumentoComercial tipoDoc){
		this.tipoDoc=tipoDoc;
	}
	
	public DocumentoComercial(){
		
	}
	
}
