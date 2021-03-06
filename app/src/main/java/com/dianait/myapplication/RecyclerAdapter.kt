package com.dianait.myapplication
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.dianait.myapplication.CellClickListener
import com.dianait.myapplication.R
import com.google.firebase.firestore.DocumentSnapshot

enum class Emoji(val src: String) {
    BUY("🛒"),
    PERSONAL("☘️"),
    PICK("↗️")
}

internal class RecyclerAdapter(private var itemsList: MutableList<DocumentSnapshot>, private val cellClickListener: CellClickListener) :

    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Define where we have to put the information
        // Title
        var itemTextView: TextView = view.findViewById(R.id.text_reminder)
        // subtitle
        var locationTextView : TextView = view.findViewById(R.id.location)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reminder, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // For each item get the info required and show it in the right place
        val item: DocumentSnapshot = itemsList[position]
        Log.d("Diana", item.id.toString())
        // Title
        var emoji: String = getEmoji(item.getString("emoji").toString())
        holder.itemTextView.text = emoji + " " + item.getString("text")
        // Subtitle
        holder.locationTextView.text = showLocation(item)

        holder.itemView.setOnClickListener {
            var idReminder = item.id
            cellClickListener.onCellClickListener(idReminder)
        }
    }
    override fun getItemCount(): Int {
        return itemsList.size
    }

    private fun showLocation(element: DocumentSnapshot): String {
        if (element.getGeoPoint("location")?.latitude == null) return "\uD83D\uDCCD Not location avaliable"
        return "\uD83D\uDCCD" + element.getGeoPoint("location")?.longitude + ", " + element.getGeoPoint("location")?.latitude
    }

    private fun getEmoji(name: String): String{
        return when (name) {
            "BUY" -> Emoji.BUY.src
            "PERSONAL" -> Emoji.PERSONAL.src
            "PICK" -> Emoji.PICK.src
            else -> Emoji.BUY.src
        }
    }
}
