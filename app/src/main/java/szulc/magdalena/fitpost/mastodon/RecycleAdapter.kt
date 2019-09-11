package szulc.magdalena.fitpost.mastodon

import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import szulc.magdalena.fitpost.R

class RecycleAdapter(private val myDataset: MutableList<MyStatus>) :
    RecyclerView.Adapter<RecycleAdapter.MyViewHolder>() {

    class MyViewHolder(val view: LinearLayout) : RecyclerView.ViewHolder(view)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false) as LinearLayout

        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val text1: TextView = holder.view.findViewById(R.id.recycle_textView1)
        val text2: TextView = holder.view.findViewById(R.id.reycle_textView2)
        val text3: TextView = holder.view.findViewById(R.id.recycle_textView3)
        val text4: TextView = holder.view.findViewById(R.id.recycle_textView4)
        text1.text = Html.fromHtml(myDataset[position].content)
        text3.text =
            " " + myDataset[position].favouritesCount.toString() + " " +
                    myDataset[position].createdAt.slice(0..9)
        if (myDataset[position].language != null && myDataset[position].visibility != null) {
            val msg =
                 myDataset[position].language + "  " + myDataset[position].visibility
            text2.text = msg
            text2.visibility = View.VISIBLE
        }else{
            text2.visibility = View.GONE
        }
        if(myDataset[position].tags!=null && myDataset[position].tags!=" "){
            text4.text =
                " " + myDataset[position].tags.toString()
            Log.d("MASTODON","textx:${text4.text}")
            text4.visibility = View.VISIBLE
        }else{
            text4.visibility = View.GONE
        }
        val image: ImageView = holder.view.findViewById(R.id.Icon)
        val imageMessage: ImageView = holder.view.findViewById(R.id.imageViewImage)
        Picasso.get().load(myDataset[position].avatar).into(image)
        if(myDataset[position].mediaAttachments!=null && myDataset[position].mediaAttachments!="") {
            Picasso.get().load(myDataset[position].mediaAttachments).into(imageMessage)
                imageMessage.visibility = View.VISIBLE
        }else{
            imageMessage.visibility = View.GONE
        }
        }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}