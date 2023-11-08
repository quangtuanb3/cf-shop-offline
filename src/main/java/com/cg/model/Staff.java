package com.cg.model;

import com.cg.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "staffs")
@Accessors(chain = true)
public class Staff extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private  String title;

    @Column
    private String phone;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_region_id", referencedColumnName = "id", nullable = false)
    private LocationRegion locationRegion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_avatar_id",referencedColumnName = "id",  nullable = false)
    private StaffAvatar staffAvatar;


    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "staff")
    @JsonIgnore
    private List<Order> orders;

//    public StaffResult toStaffDTO() {
//        return new StaffResult()
//                .setId(id)
//                .setTitle(title)
//                .setPhone(phone)
//                .setLocationRegion(locationRegion.toLocationRegionResDTO())
//                .setUser(user)
//                ;
//    }

//    public StaffUpResDTO toStaffUpResDTO() {
//        return new StaffUpResDTO()
//                .setId(id)
//                .setTitle(title)
//                .setPhone(phone)
//                .setLocationRegion(locationRegion.toLocationRegionUpResDTO())
//                .setStaffAvatar(staffAvatar)
//                ;
//    }


}
