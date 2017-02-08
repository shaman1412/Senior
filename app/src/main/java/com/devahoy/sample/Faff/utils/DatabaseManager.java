package com.devahoy.sample.Faff.utils;

//***** in CREATE_TABLE_USERPROFILE {gender type = string but in database type = char}

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.devahoy.sample.Faff.model.Bookmark;
import com.devahoy.sample.Faff.model.BookmarkList;
import com.devahoy.sample.Faff.model.Comment;
import com.devahoy.sample.Faff.model.CommentListResteraunt;
import com.devahoy.sample.Faff.model.FoodProfile;
import com.devahoy.sample.Faff.model.History;
import com.devahoy.sample.Faff.model.Marker;
import com.devahoy.sample.Faff.model.Party;
import com.devahoy.sample.Faff.model.PartyList;
import com.devahoy.sample.Faff.model.Promotion;
import com.devahoy.sample.Faff.model.PromotionList;
import com.devahoy.sample.Faff.model.Report;
import com.devahoy.sample.Faff.model.ReportList;
import com.devahoy.sample.Faff.model.Restaurant;
import com.devahoy.sample.Faff.model.RestaurantList;
import com.devahoy.sample.Faff.model.Review;
import com.devahoy.sample.Faff.model.ReviewList;
import com.devahoy.sample.Faff.model.Room;
import com.devahoy.sample.Faff.model.RoomList;
import com.devahoy.sample.Faff.model.Score;
import com.devahoy.sample.Faff.model.TypeFood;
import com.devahoy.sample.Faff.model.UserAuthen;
import com.devahoy.sample.Faff.model.UserProfile;

public class DatabaseManager extends SQLiteOpenHelper implements DatabaseManagerHelper {
    public static final String TAG = DatabaseManager.class.getSimpleName();
    private SQLiteDatabase mDatabase;

    public DatabaseManager(Context context) {
        super(context, DatabaseManagerHelper.DATABASE_NAME, null, DatabaseManagerHelper.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //region create table string

        String CREATE_TABLE_USER = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT)" ,
                UserAuthen.TABLE,
                UserAuthen.Column.ID,
                UserAuthen.Column.USERNAME,
                UserAuthen.Column.PASSWORD
        );

        String CREATE_TABLE_PROMOTION = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT not null, %s TEXT not null, %s TEXT not null, %s TEXT not null, %s TEXT not null, %s TEXT not null);" ,
                Promotion.TABLE,
                Promotion.Column.ID,
                Promotion.Column.Title,
                Promotion.Column.TitlePicture,
                Promotion.Column.StartDate,
                Promotion.Column.EndDate,
                Promotion.Column.PromotionDetail,
                Promotion.Column.Location
        );

        String CREATE_TABLE_BOOKMARK = String.format("create table if not exists %s (%s integer primary key autoincrement, %s integer not null, %s integer not null);",
                Bookmark.TABLE,
                Bookmark.Column.ID,
                Bookmark.Column.FoodID,
                Bookmark.Column.RestaurantID
        );

        String CREATE_TABLE_BOOKMARKLIST = String.format("create table if not exists %s (%s integer primary key autoincrement, %s integer not null, %s integer not null);",
                BookmarkList.TABLE,
                BookmarkList.Column.ID,
                BookmarkList.Column.BookmarkID,
                BookmarkList.Column.UserID
        );

        String CREATE_TABLE_FOODPROFILE = String.format("create table if not exists %s (%s integer primary key autoincrement, %s integer not null, %s text not null, %s text not null, %s text not null);",
                FoodProfile.TABLE,
                FoodProfile.Column.ID,
                FoodProfile.Column.restaurantID,
                FoodProfile.Column.Name,
                FoodProfile.Column.FoodPicture,
                FoodProfile.Column.FoodDetail
        );

        String CREATE_TABLE_HISTORY = String.format("create table if not exists %s (%s integer primary key autoincrement, %s integer not null, %s integer not null);",
                History.TABLE,
                History.Column.ID,
                History.Column.UserID,
                History.Column.PartyID
        );

        String CREATE_TABLE_PARTY = String.format("create table if not exists %s (%s integer primary key autoincrement, %s text not null, %s integer not null, %s text not null);",
                Party.TABLE,
                Party.Column.ID,
                Party.Column.Name,
                Party.Column.RoomID,
                Party.Column.Appointment
        );

        String CREATE_TALBLE_PARTYLIST = String.format("create table if not exists %s (%s integer primary key autoincrement, %s integer not null, %s integer not null);",
                PartyList.TABLE,
                PartyList.Column.ID,
                PartyList.Column.PartyID,
                PartyList.Column.UserID
        );

        String CREATE_TABLE_PROMOTIONLIST = String.format("create table if not exists %s (%s integer primary key autoincrement, %s integer not null, %s integer not null);",
                PromotionList.TABLE,
                PromotionList.Column.ID,
                PromotionList.Column.PromotionID,
                PromotionList.Column.UserID
        );

        String CREATE_TABLE_REPORT = String.format("create table if not exists %s (%s integer primary key autoincrement, %s integer not null, %s integer not null, %s text not null, %s text not null);",
                Report.TABLE,
                Report.Column.ID,
                Report.Column.RestaurantID,
                Report.Column.FoodID,
                Report.Column.Date,
                Report.Column.Detail
        );

        String CREATE_TABLE_REPORTLIST = String.format("create table if not exists %s (%s integer primary key autoincrement, %s integer not null, %s integer not null);",
                ReportList.TABLE,
                ReportList.Column.ID,
                ReportList.Column.ReportID,
                ReportList.Column.UserID
        );

        String CREATE_TABLE_RESTERAUNT = String.format("create table if not exists %s (%s integer primary key autoincrement, %s text not null, %s text not null, %s text not null, %s text not null, %s integer not null, %s integer not null, %s integer not null);",
                Restaurant.TABLE,
                Restaurant.Column.ID,
                Restaurant.Column.RestaurantName,
                Restaurant.Column.RestaurantPicture,
                Restaurant.Column.Location,
                Restaurant.Column.RestaurantDetail,
                Restaurant.Column.Score,
                Restaurant.Column.UserID,
                Restaurant.Column.TypeID
        );

        String CREATE_TABLE_RESTERRAUNTLIST = String.format("create table if not exists %s (%s integer primary key autoincrement, %s integer not null, %s integer not null);",
                RestaurantList.TABLE,
                RestaurantList.Column.ID,
                RestaurantList.Column.RestaurantID,
                RestaurantList.Column.UserID
        );

        String CREATE_TABLE_REVIEW = String.format("create table if not exists %s (%s integer primary key autoincrement, %s integer not null, %s integer not null, %s text not null, %s text not null, %s integer not null);",
                Review.TABLE,
                Review.Column.ID,
                Review.Column.RestaurantID,
                Review.Column.FoodID,
                Review.Column.Date,
                Review.Column.Detail,
                Review.Column.Score
        );

        String CREATE_TABLE_REVIEWLIST = String.format("create table if not exists %s (%s integer primary key autoincrement, %s integer not null, %s integer not null);",
                ReviewList.TABLE,
                ReviewList.Column.ID,
                ReviewList.Column.ReviewID,
                ReviewList.Column.UserID
        );

        String CREATE_TABLE_ROOM = String.format("create table if not exists %s (%s integer primary key autoincrement, %s integer not null, %s text not null);",
                Room.TABLE,
                Room.Column.ID,
                Room.Column.RoomID,
                Room.Column.Message
        );

        String CREATE_TABLE_ROOMLIST = String.format("create table if not exists %s (%s integer primary key autoincrement, %s integer not null);",
                RoomList.TABLE,
                RoomList.Column.ID,
                RoomList.Column.UserID
        );

        String CREATE_TABLE_SCORE = String.format("create table if not exists %s (%s integer primary key autoincrement, %s integer not null, %s integer not null, %s integer not null);",
                Score.TABLE,
                Score.Column.ID,
                Score.Column.UserID,
                Score.Column.RestaurantID,
                Score.Column.Score
        );

        String CREATE_TABLE_TYPEFOOD = String.format("create table if not exists %s (%s integer primary key autoincrement, %s integer not null);",
                TypeFood.TABLE,
                TypeFood.Column.ID,
                TypeFood.Column.Type
        );

        String CREATE_TABLE_USERPROFILE = String.format("create table if not exists %s (%s integer primary key autoincrement, %s text not null, %s text not null, %s text not null, %s integer not null, %s text not null, %s text not null, %s integer not null, %s text not null);",
                UserProfile.TABLE,
                UserProfile.Column.ID,
                UserProfile.Column.Name,
                UserProfile.Column.Address,
                UserProfile.Column.Email,
                UserProfile.Column.Telephone,
                UserProfile.Column.DateOfBirth,
                UserProfile.Column.Gender,
                UserProfile.Column.Age,
                UserProfile.Column.Picture
        );

        String CREATE_TABLE_COMMENT = String.format("create table if not exists %s (%s integer primary key autoincrement, %s integer not null, %s text not null, %s text not null);",
                Comment.TABLE,
                Comment.Column.ID,
                Comment.Column.UserID,
                Comment.Column.Comment,
                Comment.Column.Datetime
        );

        String CREATE_TABLE_COMMENTLISTRESTERAUNT = String.format("create table if not exists %s (%s integer primary key autoincrement, %s integer not null, %s integer not null);",
                CommentListResteraunt.TABLE,
                CommentListResteraunt.Column.ID,
                CommentListResteraunt.Column.CommentID,
                CommentListResteraunt.Column.ResterauntID
        );

        String CREATE_TABLE_MARKER = String.format("CREATE TABLE %s" +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT)",
                Marker.TABLE,
                Marker.column.ID,
                Marker.column.TITLE,
                Marker.column.SNIPPET,
                Marker.column.POSITION
        );



        //endregion

        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_PROMOTION);
        db.execSQL(CREATE_TABLE_BOOKMARK);
        db.execSQL(CREATE_TABLE_BOOKMARKLIST);
        db.execSQL(CREATE_TABLE_FOODPROFILE);
        db.execSQL(CREATE_TABLE_HISTORY);
        db.execSQL(CREATE_TABLE_PARTY);
        db.execSQL(CREATE_TALBLE_PARTYLIST);
        db.execSQL(CREATE_TABLE_PROMOTIONLIST);
        db.execSQL(CREATE_TABLE_REPORT);
        db.execSQL(CREATE_TABLE_REPORTLIST);
        db.execSQL(CREATE_TABLE_RESTERAUNT);
        db.execSQL(CREATE_TABLE_RESTERRAUNTLIST);
        db.execSQL(CREATE_TABLE_REVIEW);
        db.execSQL(CREATE_TABLE_REVIEWLIST);
        db.execSQL(CREATE_TABLE_ROOM);
        db.execSQL(CREATE_TABLE_ROOMLIST);
        db.execSQL(CREATE_TABLE_SCORE);
        db.execSQL(CREATE_TABLE_TYPEFOOD);
        db.execSQL(CREATE_TABLE_USERPROFILE);
        db.execSQL(CREATE_TABLE_COMMENT);
        db.execSQL(CREATE_TABLE_COMMENTLISTRESTERAUNT);
        db.execSQL(CREATE_TABLE_MARKER); // สร้าง map database
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_USER = "DROP TABLE IF EXISTS " + DatabaseManagerHelper.DATABASE_VERSION;
        db.execSQL(DROP_USER);
        onCreate(mDatabase);
    }

    //region UserAuthen
    @Override
    public long registerUser(UserAuthen userAuthen) {

        mDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserAuthen.Column.USERNAME, userAuthen.getUsername());
        values.put(UserAuthen.Column.PASSWORD, userAuthen.getPassword());

        long result = mDatabase.insert(UserAuthen.TABLE, null, values);
        mDatabase.close();

        return result;
    }

    @Override
    public UserAuthen checkUserLogin(UserAuthen userAuthen) {

        mDatabase = this.getReadableDatabase();

        Cursor cursor = mDatabase.query(UserAuthen.TABLE,
                null,
                UserAuthen.Column.USERNAME + " = ? AND " +
                        UserAuthen.Column.PASSWORD + " = ?",
                new String[]{userAuthen.getUsername(), userAuthen.getPassword()},
                null,
                null,
                null);

        UserAuthen currentUserAuthen = new UserAuthen();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                currentUserAuthen.setId((int) cursor.getLong(0));
                currentUserAuthen.setUsername(cursor.getString(1));
                currentUserAuthen.setPassword(cursor.getString(2));
                mDatabase.close();
                return currentUserAuthen;
            }
        }

        return null;
    }

    @Override
    public int changePassword(UserAuthen userAuthen) {

        mDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserAuthen.Column.USERNAME, userAuthen.getUsername());
        values.put(UserAuthen.Column.PASSWORD, userAuthen.getPassword());

        int row = mDatabase.update(UserAuthen.TABLE,
                values,
                UserAuthen.Column.ID + " = ?",
                new String[] {String.valueOf(userAuthen.getId())});

        mDatabase.close();
        return row;
    }
    //endregion

    //region Promotion
    public long addPromotion(Promotion promotion) {

        mDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Promotion.Column.Title, promotion.getTitle());
        values.put(Promotion.Column.TitlePicture, promotion.getTitlePictureLink());
        values.put(Promotion.Column.StartDate, promotion.getStartDate());
        values.put(Promotion.Column.EndDate, promotion.getEndDate());
        values.put(Promotion.Column.PromotionDetail, promotion.getPromotionDetail());
        values.put(Promotion.Column.Location, promotion.getGoogleMapLink());

        long result = mDatabase.insert(Promotion.TABLE, null, values);
        mDatabase.close();

        return result;
    }

    public Cursor getAllPromotion()
    {
        mDatabase = this.getWritableDatabase();
        return mDatabase.query(Promotion.TABLE, new String[] {Promotion.Column.ID, Promotion.Column.Title, Promotion.Column.TitlePicture, Promotion.Column.StartDate, Promotion.Column.EndDate, Promotion.Column.PromotionDetail, Promotion.Column.Location}, null, null, null, null, null);
    }

    public void close(){
        mDatabase.close();
    }
    //endregion
}