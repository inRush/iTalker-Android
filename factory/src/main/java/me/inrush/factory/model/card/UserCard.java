//package me.inrush.factory.model.card;
//
//
//import com.google.gson.annotations.Expose;
//
//import me.inrush.factory.model.db.User;
//
///**
// * @author inrush
// * @date 2017/7/24.
// * @package me.inrush.italker.bean.card
// */
//public class UserCard {
//    @Expose
//    private String id;
//    @Expose
//    private String name;
//    @Expose
//    private String phone;
//    @Expose
//    private String portrait;
//    @Expose
//    private String desc;
//    @Expose
//    private int sex = 0;
//
//    // 粉丝的数量
//    @Expose
//    private int follows;
//
//    // 我关注人的数量
//    @Expose
//    private int following;
//
//    // 我与当前user关系状态,是否已经关注此人
//    @Expose
//    private boolean isFollow;
//
//    // 用户信息最后更新时间
//    @Expose
//    private LocalDateTime modifyAt;
//
//    public UserCard(){
//
//    }
//
//    public UserCard(final User user) {
//        this(user,false);
//    }
//
//    public UserCard(final User user, boolean isFollow) {
//        this.id = user.getId();
//        this.name = user.getName();
//        this.phone = user.getPhone();
//        this.portrait = user.getPortrait();
//        this.desc = user.getDescription();
//        this.sex = user.getSex();
//        this.modifyAt = user.getUpdateAt();
//
//        // TODO 得到关注人和粉丝的数量
//        //user.getFollowers().size(); 懒加载会报错,因为没有Session
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getPortrait() {
//        return portrait;
//    }
//
//    public void setPortrait(String portrait) {
//        this.portrait = portrait;
//    }
//
//    public String getDesc() {
//        return desc;
//    }
//
//    public void setDesc(String desc) {
//        this.desc = desc;
//    }
//
//    public int getSex() {
//        return sex;
//    }
//
//    public void setSex(int sex) {
//        this.sex = sex;
//    }
//
//    public int getFollows() {
//        return follows;
//    }
//
//    public void setFollows(int follows) {
//        this.follows = follows;
//    }
//
//    public int getFollowing() {
//        return following;
//    }
//
//    public void setFollowing(int following) {
//        this.following = following;
//    }
//
//    public boolean isFollow() {
//        return isFollow;
//    }
//
//    public void setFollow(boolean follow) {
//        isFollow = follow;
//    }
//
//    public LocalDateTime getModifyAt() {
//        return modifyAt;
//    }
//
//    public void setModifyAt(LocalDateTime modifyAt) {
//        this.modifyAt = modifyAt;
//    }
//}
