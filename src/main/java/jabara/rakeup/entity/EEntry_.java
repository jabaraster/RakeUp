package jabara.rakeup.entity;

import jabara.jpa.entity.EntityBase_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2012-12-24T01:44:50.405+0900")
@StaticMetamodel(EEntry.class)
public class EEntry_ extends EntityBase_ {
	public static volatile SingularAttribute<EEntry, String> text;
	public static volatile ListAttribute<EEntry, EKeyword> keywords;
	public static volatile SingularAttribute<EEntry, ESource> source;
	public static volatile SingularAttribute<EEntry, String> title;
}
