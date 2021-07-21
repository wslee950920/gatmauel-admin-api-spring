package com.gatmauel.admin.entity.category;

import com.gatmauel.admin.dto.food.FoodDTO;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Getter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.gatmauel.admin.entity.food.Food;

@ToString(exclude="foodList")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="category",
        uniqueConstraints={
                @UniqueConstraint(
                        columnNames={"category", "prior"}
                )
        })
public class Category {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(length=20, nullable=false)
    private String category;

    @Column(nullable=false)
    private int prior;

    @Builder.Default
    //CascadeType.PERSIST는 update시 전파 안 된다...
    @OneToMany(fetch=FetchType.LAZY, mappedBy="category", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Food> foodList=new ArrayList<>();

    public void addFood(Food food) {
        food.setCategory(this);
        foodList.add(food);
    }

    public void removeFood(Food food){
        food.setCategory(null);
        foodList.remove(food);
    }

    public void changeCategory(String category) {
        this.category=category;
    }

    public void changePrior(int prior) {
        this.prior=prior;
    }
}