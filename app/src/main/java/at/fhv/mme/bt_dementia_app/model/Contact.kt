package at.fhv.mme.bt_dementia_app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "contacts",
    indices = [Index(
        value = ["name", "relation", "phone_number", "profile_image_path"],
        unique = true
    )]
)
data class Contact(
    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "relation")
    val relation: String,

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,

    @ColumnInfo(name = "profile_image_path")
    val profileImagePath: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null
)