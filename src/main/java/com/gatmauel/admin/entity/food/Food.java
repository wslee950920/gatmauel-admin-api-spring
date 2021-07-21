package com.gatmauel.admin.entity.food;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Getter;
import lombok.AllArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.gatmauel.admin.entity.category.Category;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude="category")
@Entity
@Table(name="food",
        uniqueConstraints={
                @UniqueConstraint(
                        columnNames={"name", "prior"}
                )
        })
public class Food {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(length=20, nullable=false)
    private String name;

    @Column(length=20, nullable=false)
    private String img;

    @Column(nullable=false)
    private int price;

    @Column(nullable=false)
    private int prior;

    @Builder.Default
    @Column(columnDefinition="boolean default false", nullable=false)
    private boolean deli=false;

    @Column(columnDefinition = "text")
    private String comp;

    @ManyToOne(fetch=FetchType.LAZY)
    private Category category;

    public void setCategory(Category category) {
        this.category=category;
    }

    public void changeName(String name){
        this.name=name;
    }

    public void changeImg(String img){
        this.img=img;
    }

    public void changePrice(int price){
        this.price=price;
    }

    public void changePrior(int prior){
        this.prior=prior;
    }

    public void changeDeli(boolean deli){
        this.deli=deli;
    }

    public void changeComp(String comp){
        this.comp=comp;
    }
}