package com.gatmauel.admin.entity.admin;

import com.gatmauel.admin.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Getter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name="admins")
public class Admin extends BaseEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(length=40, nullable=false, unique=true)
    private String email;

    @Builder.Default
    @Column(columnDefinition="boolean default false", nullable=false)
    private boolean eVerified=false;

    @Builder.Default
    @Column(columnDefinition="varchar(20) default '사장님'", nullable=false)
    private String nick="사장님";

    @Column(length=100)
    private String hashedPassword;

    @ElementCollection(fetch=FetchType.LAZY)
    @Builder.Default
    private Set<AdminRole> roleSet=new HashSet<>();

    public void addAdminRole(AdminRole adminRole){
        roleSet.add(adminRole);
    }

    public void changePassword(String hashedPassword){
        this.hashedPassword=hashedPassword;
    }

    public void changeEVerified(boolean eVerified){
        this.eVerified=eVerified;
    }
}