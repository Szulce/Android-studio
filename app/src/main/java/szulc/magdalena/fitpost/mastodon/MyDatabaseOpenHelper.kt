package szulc.magdalena.fitpost
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*


data class MyStatus(
    val id :Long,
    val content: String,
    val avatar: String?,
    val favouritesCount:Long,
    val reblogsCount:Long,
    val language:String?,
    val visibility:String?,
    val createdAt:String
)

class MyDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MyDatabase", null, MyDatabaseOpenHelper.DB_VERSION) {
    init {
        instance = this
    }

    companion object {
        private var instance: MyDatabaseOpenHelper? = null
        val DB_VERSION = 7

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper{
            if(instance == null){
                instance = MyDatabaseOpenHelper(ctx.getApplicationContext())
            }
            return instance!!
        }

        fun getStatus(db: SQLiteDatabase):List<MyStatus> = db.use {
            val rowParser = classParser<MyStatus>()
            db.select("MyStatus").orderBy("id",SqlOrderDirection.DESC).exec { parseList(rowParser) }
        }

        val TABLE_STATUS= "MyStatus"
        val COLUMNS = arrayOf<String>(
            "id",
            "content",
            "avatar",
            "favouritesCount",
            "reblogsCount",
            "language",
            "visibility",
            "createdAt"
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
            "createdAt" to INTEGER)
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
