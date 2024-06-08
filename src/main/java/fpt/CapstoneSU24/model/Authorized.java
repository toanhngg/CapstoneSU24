package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

    @Entity
    @Table(name = "authorized")
    public class Authorized {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "authorized_id")
        private int authorized_id;
        @Column(name = "authorized_name")
        private String authorized_name;
        @Column(name = "authorized_email")
        private String authorized_email;
        @Column(name = "assign_person")
        private String assign_person;
        @Column(name = "assign_person_mail")
        private String assign_person_mail;
        @ManyToOne
        @JoinColumn(name = "location_id")
        private Location location;
        @Column(name = "description", columnDefinition = "nvarchar(255)")
        private String description;
        @Column(name = "phone_number")
        private String phoneNumber;

        public Authorized(String authorized_name, String authorized_email, String assign_person, String assign_person_mail, Location location, String description, String phoneNumber) {
            this.authorized_name = authorized_name;
            this.authorized_email = authorized_email;
            this.assign_person = assign_person;
            this.assign_person_mail = assign_person_mail;
            this.location = location;
            this.description = description;
            this.phoneNumber = phoneNumber;
        }

        public Authorized(String authorized_name, String authorized_email, Location location, String description, String phoneNumber) {
            this.authorized_name = authorized_name;
            this.authorized_email = authorized_email;
            this.location = location;
            this.description = description;
            this.phoneNumber = phoneNumber;
        }

        public String getAssign_person() {
            return assign_person;
        }

        public void setAssign_person(String assign_person) {
            this.assign_person = assign_person;
        }

        public String getAssign_person_mail() {
            return assign_person_mail;
        }

        public void setAssign_person_mail(String assign_person_mail) {
            this.assign_person_mail = assign_person_mail;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public Authorized() {
        }


        public int getAuthorized_id() {
            return authorized_id;
        }

        public void setAuthorized_id(int authorized_id) {
            this.authorized_id = authorized_id;
        }

        public String getAuthorized_name() {
            return authorized_name;
        }

        public void setAuthorized_name(String authorized_name) {
            this.authorized_name = authorized_name;
        }

        public String getAuthorized_email() {
            return authorized_email;
        }

        public void setAuthorized_email(String authorized_email) {
            this.authorized_email = authorized_email;
        }

    }
