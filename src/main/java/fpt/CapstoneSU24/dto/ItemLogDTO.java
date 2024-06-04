package fpt.CapstoneSU24.dto;

import fpt.CapstoneSU24.model.*;
import lombok.Data;

@Data
public class ItemLogDTO {
        private int itemLogId;
        private String address;
        private String city;
        private String country;
        private String coordinateX;
        private String coordinateY;

        private long createdAt;
        private String phone;
        private String orgName;
        private String fullName;
        private String supportingDocuments;
        private String descriptionOrigin;

        private int productId;
        private String productRecognition;
        private int statusProduct;

        private String descriptionParty;
        private String email;
        private String signature;

        private String descriptionItemLog;
        private String eventType;
        private int statusItemLog;
        private Object imageItemLog;

        public int getItemLogId() {
                return itemLogId;
        }

        public void setItemLogId(int itemLogId) {
                this.itemLogId = itemLogId;
        }

        // Getters and Setters
        public String getAddress() {
                return address;
        }

        public void setAddress(String address) {
                this.address = address;
        }

        public String getCity() {
                return city;
        }

        public void setCity(String city) {
                this.city = city;
        }

        public String getCountry() {
                return country;
        }

        public void setCountry(String country) {
                this.country = country;
        }

        public String getCoordinateX() {
                return coordinateX;
        }

        public void setCoordinateX(String coordinateX) {
                this.coordinateX = coordinateX;
        }

        public String getCoordinateY() {
                return coordinateY;
        }

        public void setCoordinateY(String coordinateY) {
                this.coordinateY = coordinateY;
        }

        public long getCreatedAt() {
                return createdAt;
        }

        public void setCreatedAt(long createdAt) {
                this.createdAt = createdAt;
        }

        public String getPhone() {
                return phone;
        }

        public void setPhone(String phone) {
                this.phone = phone;
        }

        public String getOrgName() {
                return orgName;
        }

        public void setOrgName(String orgName) {
                this.orgName = orgName;
        }

        public String getFullName() {
                return fullName;
        }

        public void setFullName(String fullName) {
                this.fullName = fullName;
        }

        public String getSupportingDocuments() {
                return supportingDocuments;
        }

        public void setSupportingDocuments(String supportingDocuments) {
                this.supportingDocuments = supportingDocuments;
        }

        public String getDescriptionOrigin() {
                return descriptionOrigin;
        }

        public void setDescriptionOrigin(String descriptionOrigin) {
                this.descriptionOrigin = descriptionOrigin;
        }

        public int getProductId() {
                return productId;
        }

        public void setProductId(int productId) {
                this.productId = productId;
        }

        public String getProductRecognition() {
                return productRecognition;
        }

        public void setProductRecognition(String productRecognition) {
                this.productRecognition = productRecognition;
        }

        public int getStatusProduct() {
                return statusProduct;
        }

        public void setStatusProduct(int statusProduct) {
                this.statusProduct = statusProduct;
        }

        public String getDescriptionParty() {
                return descriptionParty;
        }

        public void setDescriptionParty(String descriptionParty) {
                this.descriptionParty = descriptionParty;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getSignature() {
                return signature;
        }

        public void setSignature(String signature) {
                this.signature = signature;
        }

        public String getDescriptionItemLog() {
                return descriptionItemLog;
        }

        public void setDescriptionItemLog(String descriptionItemLog) {
                this.descriptionItemLog = descriptionItemLog;
        }

        public String getEventType() {
                return eventType;
        }

        public void setEventType(String eventType) {
                this.eventType = eventType;
        }

        public int getStatusItemLog() {
                return statusItemLog;
        }

        public void setStatusItemLog(int statusItemLog) {
                this.statusItemLog = statusItemLog;
        }

        public Object getImageItemLog() {
                return imageItemLog;
        }

        public void setImageItemLog(Object imageItemLog) {
                this.imageItemLog = imageItemLog;
        }
}
