package zone.yby.ecar.entity;

import javax.persistence.*;

@Entity
@Table(name = "rank")
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "vid")
    private Integer vid;

    @Column(name = "name")
    private String name;

    @Column(name = "num")
    private String num;

    @Column(name = "type")
    private String type;

    public Rank() {
    }

    public Rank(Integer vid, String name, String num, String type) {
        this.vid = vid;
        this.name = name;
        this.num = num;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public Integer getVid() {
        return vid;
    }

    public String getName() {
        return name;
    }

    public String getNum() {
        return num;
    }

    public String getType() {
        return type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setVid(Integer vid) {
        this.vid = vid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "id=" + id +
                ", vid=" + vid +
                ", name='" + name + '\'' +
                ", num='" + num + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
