package space.beka.newpasport.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Person : Serializable {


    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var name: String? = null
    var lastName: String? = null
    var image: String? = null


    constructor(name: String?, lastName: String?, image: String?) {
        this.name = name
        this.lastName = lastName
        this.image = image
    }
    constructor()
}