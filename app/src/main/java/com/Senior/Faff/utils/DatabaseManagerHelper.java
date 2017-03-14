package com.Senior.sample.Faff.utils;

import com.Senior.sample.Faff.model.UserAuthen;

public interface DatabaseManagerHelper {

    public static final String DATABASE_NAME = "Faff_DB";
    public static final int DATABASE_VERSION = 1;

    /**
     * ทำการเซฟข้อมูล UserAuthen ลงฐานข้อมูล
     * @param userAuthen
     * @return หากบันทึกสำเร็จจะส่งค่า row ID กลับมา ถ้ามี error จะส่ง -1
     */
    public long registerUser(UserAuthen userAuthen);

    /**
     * ทำการเช็ค UserAuthen ว่าล็อคอินด้วย username และ password <br />
     * ถูกต้องตรงกับในฐานข้อมูลหรือไม่ (มันก็คือการ query sqlite นั่นเอง) <br />
     * หาก query ด้วย username, password แล้วมีข้อมูล แสดงว่า ล็อคอินถูกต้อง
     * @param userAuthen
     * @return - หากตรง ก็ส่งค่าเป็น userAuthen นั้นๆกลับไป หากไม่ตรงก็ส่ง null
     */
    public UserAuthen checkUserLogin(UserAuthen userAuthen);

    /**
     * สำหรับเปลี่ยน password โดยทำการ query หาข้อมูล username, password ก่อน <br />
     * จากนั้นถึง update โดยเปลี่ยน password ใหม่แทน
     * @param userAuthen
     * @return - ส่งค่า จำนวนแถวที่มีการ update
     */
    public int changePassword(UserAuthen userAuthen);
}
