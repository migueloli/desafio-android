package br.com.boxdelivery.github_javapop.controller.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.boxdelivery.github_javapop.R
import br.com.boxdelivery.github_javapop.model.GithubPullRequestModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class GithubPullRequestsAdapter(
    private var items: MutableList<GithubPullRequestModel?>,
    private val onClick : View.OnClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_LOADING = 1
    }

    lateinit var ctx : Context

    fun addData(list: MutableList<GithubPullRequestModel>?) {
        if(list != null) {
            removeLoadingView()
            this.items.addAll(list)
            notifyDataSetChanged()
        }
    }

    fun getItemAtPosition(position: Int): GithubPullRequestModel? {
        return items[position]
    }

    fun addLoadingView() {
        items.add(null)
        notifyItemInserted(items.size - 1)
    }

    fun removeLoadingView() {
        if (items.size != 0) {
            items.removeAt(items.size - 1)
            notifyItemRemoved(items.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        ctx = parent.context
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.github_pull_item,
                parent,
                false
            )
            ItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(ctx).inflate(R.layout.progress_item, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int)
            = if (items[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val git = items[position]
        if (holder.itemViewType == VIEW_TYPE_ITEM && holder is ItemViewHolder && git != null) {
            holder.apply {
                Picasso.get().load(git.user.avatar_url).error(R.drawable.user).into(profile)

                username.text = git.user.login
                title.text = git.title
                body.text = git.body
                created.text = SimpleDateFormat(
                    "dd/MM/yyyy HH:mm:ss",
                    Locale("pt", "BR")
                ).format(git.created_at)
            }
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profile : CircleImageView = itemView.findViewById(R.id.imv_profile)
        val username : TextView = itemView.findViewById(R.id.txv_username)
        val title : TextView = itemView.findViewById(R.id.txv_title)
        val body : TextView = itemView.findViewById(R.id.txv_body)
        val created : TextView = itemView.findViewById(R.id.txv_created)

        init {
            itemView.setOnClickListener(onClick)
        }
    }

}