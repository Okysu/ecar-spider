package zone.yby.ecar.entity;

import javax.persistence.*;

@Entity
@Table(name = "appraisal")
public class Appraisal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "localGuidePrice")
    private String localGuidePrice;

    @Column(name = "factoryGuidePrice")
    private String factoryGuidePrice;

    @Column(name = "namespace")
    private String namespace;

    public Appraisal() {}

    public Appraisal(String localGuidePrice, String factoryGuidePrice, String namespace) {
        this.localGuidePrice = localGuidePrice;
        this.factoryGuidePrice = factoryGuidePrice;
        this.namespace = namespace;
    }

    public Integer getId() {
        return id;
    }

    public String getLocalGuidePrice() {
        return localGuidePrice;
    }

    public String getFactoryGuidePrice() {
        return factoryGuidePrice;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLocalGuidePrice(String localGuidePrice) {
        this.localGuidePrice = localGuidePrice;
    }

    public void setFactoryGuidePrice(String factoryGuidePrice) {
        this.factoryGuidePrice = factoryGuidePrice;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String toString() {
        return "Appraisal{" +
                "id=" + id +
                ", localGuidePrice='" + localGuidePrice + '\'' +
                ", factoryGuidePrice='" + factoryGuidePrice + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
