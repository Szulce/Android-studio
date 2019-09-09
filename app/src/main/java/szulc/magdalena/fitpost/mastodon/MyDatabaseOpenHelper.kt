package szulc.magdalena.fitpost.mastodon
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import org.jetbrains.anko.db.*


data class MyStatus(
    val id :Long,
    val content: String,
    val avatar: String?,
    val favouritesCount:Long,
    val reblogsCount:Long,
    val language:String?,
    val visibility:String?,
    val createdAt:String,
    val attachedImage:Bitmap
)

class MyDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MyDatabase", null, DB_VERSION) {
    init {
        instance = this
    }

    companion object {
        private var instance: MyDatabaseOpenHelper? = null
        const val DB_VERSION = 9

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            if(instance == null){
                instance = MyDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance!!
        }

        fun getStatus(db: SQLiteDatabase):List<MyStatus> = db.use {
            val rowParser = classParser<MyStatus>()
            db.select("MyStatus").orderBy("id",SqlOrderDirection.DESC).exec { parseList(rowParser) }
        }

        const val TABLE_STATUS= "MyStatus"
        val COLUMNS = arrayOf(
            "id",
            "content",
            "avatar",
            "favouritesCount",
            "reblogsCount",
            "language",
            "visibility",
            "createdAt",
            "attachedImage"
        )
    }


    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable("MyStatus", true,
            "id" to INTEGER + PRIMARY_KEY + UNIQUE,
            "content" to TEXT,
            "avatar"  to TEXT + DEFAULT("'https://img.icons8.com/wired/64/000000/user.png'"),
            "favouritesCount" to INTEGER,
            "reblogsCount" to INTEGER,
            "language" to TEXT+ DEFAULT("'en'"),
            "visibility" to TEXT+ DEFAULT("'public'"),
            "createdAt" to INTEGER,
            "attachedImage" to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable("MyStatus", true)
        onCreate(db)
    }
}

// Access property for Context
val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(this)
