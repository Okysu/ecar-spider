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

    @Column(name = "price")
    private String price;

    @Column(name = "vid")
    private Integer vid;

    public Model() {}

    public Model(Integer mid, String name, String price, Integer vid) {
        this.mid = mid;
        this.name = name;
        this.price = price;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
                ", price='" + price + '\'' +
                ", vid=" + vid +
                '}';
    }
}