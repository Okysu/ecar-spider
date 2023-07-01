package zone.yby.ecar.entity;

import javax.persistence.*;

/**
 * 品牌实体类
 * @author 于柏杨(160521118)
 * @since 2023-06-30 12:30:00
 */
@Entity
@Table(name = "brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "mid")
    private Integer mid;

    @Column(name = "name")
    private String name;

    public Brand() {}

    public Brand(Integer mid, String name) {
        this.mid = mid;
        this.name = name;
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

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", mid=" + mid +
                ", name='" + name + '\'' +
                '}';
    }
}