//package covid19.care.feature.home
//
//import android.graphics.drawable.Drawable
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.ProgressBar
//import android.widget.RelativeLayout
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.DataSource
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.load.engine.GlideException
//import com.bumptech.glide.request.RequestListener
//import com.bumptech.glide.request.target.Target
//import covid19.care.R
//
//class StagesAdapter(var catarray: ArrayList<String>?=null) : RecyclerView.Adapter<StagesAdapter.ViewHolder>() {
//
//    var rowindex:Int = -1
//    private var onItemClickListener: ItemClickListener? = null
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.stagerview, parent, false)
//        return ViewHolder(v)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.dayago.text = catModel.day
//    }
//
//    override fun getItemCount(): Int {
//        return catarray!!.size
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return position
//    }
//
//    fun setItemClickListener(clickListener: ItemClickListener) {
//        onItemClickListener = clickListener
//    }
//
//
//    interface ItemClickListener {
//        fun onItemClick(
//            view: View,
//            position: Int,
//            postId: String,
//            type: String,
//            categoryId: String
//        )
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val dayago: TextView = itemView.findViewById(R.id.dayago)
//    }
//}