package acme.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AuditRecord extends AbstractEntity {

	protected static final long serialVersionUID = 1L;
	
	@NotNull
	@NotBlank
	@Length( max = 75 )
	protected String subject;
	
	@NotNull
	@NotBlank
	@Length( max = 100 )
	protected String assesment;
	
	@NotNull
	@NotBlank
	@Pattern( regexp = "(?:A\\+|A|B|C|D|F|F\\-)" )
	protected String mark;
	
	@NotNull
	@Digits( integer = 1, fraction = 2 )
	@Range( min = (long) 0.01, max = 1 )
	protected double period;
	
	
	@NotBlank
	@URL
	protected String moreInfo;

	@NotNull
	@ManyToOne( optional = false )
	protected Audit audit;
}
