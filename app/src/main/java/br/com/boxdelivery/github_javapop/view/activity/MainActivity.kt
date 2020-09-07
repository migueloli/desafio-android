package br.com.boxdelivery.github_javapop.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.boxdelivery.github_javapop.R
import br.com.boxdelivery.github_javapop.controller.adapter.GithubRepositoryAdapter
import br.com.boxdelivery.github_javapop.controller.listener.RecyclerViewScrollLoad
import br.com.boxdelivery.github_javapop.model.GithubRepositoryModel
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), RecyclerViewScrollLoad.OnLoadMoreListener {

    private var items = mutableListOf<GithubRepositoryModel?>()
    private var layoutManager = LinearLayoutManager(this)
    private var job : Job? = null

    private lateinit var adapter: GithubRepositoryAdapter
    private lateinit var scrollListener: RecyclerViewScrollLoad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        this.prepareRecyclerView()
        this.setScrollListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    // Function to configure the recycler view for the activity.
    private fun prepareRecyclerView() {
        adapter = GithubRepositoryAdapter(items)
        adapter.notifyDataSetChanged()
        rcv_list.adapter = adapter
        rcv_list.layoutManager = layoutManager
        rcv_list.setHasFixedSize(true)
    }

    // Function to configure the scroll listener for the recycler view.
    private fun setScrollListener() {
        scrollListener = RecyclerViewScrollLoad(layoutManager)
        scrollListener.setOnLoadMoreListener(this)
        rcv_list.addOnScrollListener(scrollListener)
    }

    // Function to load more data for the list.
    override fun onLoadMore() {
        adapter.addLoadingView()
        job = GlobalScope.launch {
            adapter.removeLoadingView()
        }
    }

}