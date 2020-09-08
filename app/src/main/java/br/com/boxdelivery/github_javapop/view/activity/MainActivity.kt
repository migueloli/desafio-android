package br.com.boxdelivery.github_javapop.view.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.boxdelivery.github_javapop.R
import br.com.boxdelivery.github_javapop.controller.adapter.GithubRepositoryAdapter
import br.com.boxdelivery.github_javapop.controller.api.GithubEndpoints
import br.com.boxdelivery.github_javapop.controller.api.RetrofitUtils
import br.com.boxdelivery.github_javapop.controller.listener.RecyclerViewScrollLoad
import br.com.boxdelivery.github_javapop.model.GithubBody
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(),
    RecyclerViewScrollLoad.OnLoadMoreListener,
    Callback<GithubBody>, View.OnClickListener {

    private var layoutManager = LinearLayoutManager(this)
    private var nextPage = 1
    private val endpoints = RetrofitUtils.getRetrofitInstance().create(GithubEndpoints::class.java)
    private var retries = 0

    private lateinit var adapter: GithubRepositoryAdapter
    private lateinit var scrollListener: RecyclerViewScrollLoad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        this.prepareRecyclerView()
        this.setScrollListener()
    }

    /* ------ Controllers for activity ------*/

    // Function to configure the recycler view for the activity.
    private fun prepareRecyclerView() {
        adapter = GithubRepositoryAdapter(mutableListOf(), this)
        adapter.notifyDataSetChanged()
        rcv_repositories.adapter = adapter
        rcv_repositories.layoutManager = layoutManager
        rcv_repositories.setHasFixedSize(true)
        onLoadMore()
    }

    // Function to configure the scroll listener for the recycler view.
    private fun setScrollListener() {
        scrollListener = RecyclerViewScrollLoad(layoutManager)
        scrollListener.setOnLoadMoreListener(this)
        rcv_repositories.addOnScrollListener(scrollListener)
    }

    /* ------ OnScrollListener ------*/

    // Function to load more data for the list.
    override fun onLoadMore() {
        adapter.addLoadingView()

        retries = 0
        endpoints.getRepositories(nextPage).enqueue(this)
    }

    /* ------ RecyclerView OnClick ------*/

    override fun onClick(v: View?) {
        if(v != null) {
            val pos = rcv_repositories.getChildLayoutPosition(v)
            val repository = adapter.getItemAtPosition(pos)

            if(repository != null) {
                startActivity(PullRequestActivity.newIntent(this, repository))
            }
        }
    }

    /* ------ Retrofit Callback for GithubRepositoriesModel ------*/

    override fun onResponse(
        call: Call<GithubBody>,
        response: Response<GithubBody>
    ) {
        if(response.isSuccessful) {
            nextPage++
            adapter.addData(response.body()?.items)
            scrollListener.setLoaded()
        }
    }

    override fun onFailure(call: Call<GithubBody>, t: Throwable) {
        t.printStackTrace()

        var message = "Máximo de tentativas atingido."

        if (retries < 2) {
            retries++
            message = "Ocorreu um erro, tentando novamente."
            endpoints.getRepositories(nextPage).enqueue(this)
        } else {
            adapter.removeLoadingView()
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}