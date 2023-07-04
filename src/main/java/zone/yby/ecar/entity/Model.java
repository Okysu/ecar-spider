package zone.yby.ecar.entity;

import javax.persistence.*;

/**
 * 车型实体类
 * @author 于柏杨(160521118)
 * @since 2023-06-30 12:30:00
 */
@Entity
@Table(name = "model")
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "mid")
    private Integer mid;

    @Column(name = "name")
    private String name;

    @Column(name = "namespace")
    private String namespace;

    @Column(name = "vid")
    private Integer vid;

    public Model() {
    }

    public Model(Integer mid, String name, String namespace, Integer vid) {
        this.mid = mid;
        this.name = name;
        this.namespace = namespace;
        this.vid = vid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Integer getVid() {
        return vid;
    }

    public void setVid(Integer vid) {
        this.vid = vid;
    }

    @Override
    public String toString() {
        return "Model{" +
                "id=" + id +
                ", mid=" + mid +
                ", name='" + name + '\'' +
                ", namespace='" + namespace + '\'' +
                ", vid=" + vid +
                '}';
    }
}