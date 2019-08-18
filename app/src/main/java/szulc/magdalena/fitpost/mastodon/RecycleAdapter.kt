package szulc.magdalena.fitpost

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RecycleAdapter(private val myDataset: MutableList<MyStatus>) :
    RecyclerView.Adapter<RecycleAdapter.MyViewHolder>() {

    class MyViewHolder(val view: LinearLayout) : RecyclerView.ViewHolder(view)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecycleAdapter.MyViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false) as LinearLayout

        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var text1:TextView= holder.view.findViewById(R.id.recycle_textView1)
        var text2:TextView= holder.view.findViewById(R.id.reycle_textView2)
        var text3:TextView= holder.view.findViewById(R.id.recycle_textView3)
        text1.text = Html.fromHtml(myDataset[position].content)
        text2.text = "Fav:"+myDataset[position].favouritesCount.toString()+"Reblogs:"+myDataset[position].reblogsCount.toString()
        var msg = myDataset[position].language+" "+myDataset[position].visibility
        text3.text = msg
        var image:ImageView = holder.view.findViewById(R.id.Icon)
        Picasso.get().load(myDataset[position].avatar).into(image)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}