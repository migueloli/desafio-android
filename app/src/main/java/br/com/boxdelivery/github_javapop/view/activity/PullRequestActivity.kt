package br.com.boxdelivery.github_javapop.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.boxdelivery.github_javapop.R
import br.com.boxdelivery.github_javapop.controller.adapter.GithubPullRequestsAdapter
import br.com.boxdelivery.github_javapop.controller.api.GithubEndpoints
import br.com.boxdelivery.github_javapop.controller.api.RetrofitUtils
import br.com.boxdelivery.github_javapop.model.GithubPullRequestModel
import br.com.boxdelivery.github_javapop.model.GithubRepositoryModel
import kotlinx.android.synthetic.main.content_pull_request.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PullRequestActivity : AppCompatActivity(),
    Callback<MutableList<GithubPullRequestModel>>, View.OnClickListener {

    companion object {
        private const val GITHUB_REPOSITORY_KEY = "github_repository_key"

        @JvmStatic
        fun newIntent(activity: AppCompatActivity, repository: GithubRepositoryModel) : Intent{
            val intent = Intent(activity, PullRequestActivity::class.java)
            intent.putExtra(GITHUB_REPOSITORY_KEY, repository)
            return intent
        }
    }

    private var layoutManager = LinearLayoutManager(this)
    private val endpoints = RetrofitUtils.getRetrofitInstance().create(GithubEndpoints::class.java)
    private var retries = 0

    private lateinit var repository: GithubRepositoryModel
    private lateinit var adapter: GithubPullRequestsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pull_request)
        setSupportActionBar(findViewById(R.id.toolbar))
        
        if(hasRepository()) {
            title = repository.getTitle()

            prepareRecyclerView()
        }
    }

    /* ------ Controllers for activity ------*/

    /** Function to get GithubRepositoryModel for intent
     * and return a boolean if has a valid repository or not.
     */
    private fun hasRepository() : Boolean {
        val repo = intent.getParcelableExtra<GithubRepositoryModel>(GITHUB_REPOSITORY_KEY)
        if(repo == null) {
            finish()
            return false
        }

        repository = repo
        return true
    }

    // Function to configure the recycler view for the activity.
    private fun prepareRecyclerView() {
        adapter = GithubPullRequestsAdapter(mutableListOf(), this)
        adapter.notifyDataSetChanged()
        rcv_requests.adapter = adapter
        rcv_requests.layoutManager = layoutManager
        rcv_requests.setHasFixedSize(true)
        onLoadData()
    }

    // Function to load data for the list.
    private fun onLoadData() {
        adapter.addLoadingView()

        retries = 0
        endpoints.getPullRequests(repository.owner.login, repository.name).enqueue(this)
    }

    /* ------ RecyclerView OnClick ------*/

    override fun onClick(v: View?) {
        if(v != null) {
            val pos = rcv_requests.getChildLayoutPosition(v)
            val request = adapter.getItemAtPosition(pos)

            if(request != null) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(request.html_url)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this,
                        "Não foi possível encontrar nenhuma aplicação para abrir este site.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /* ------ Retrofit Callback for GithubRepositoriesModel ------*/

    override fun onResponse(
        call: Call<MutableList<GithubPullRequestModel>>,
        response: Response<MutableList<GithubPullRequestModel>>
    ) {
        if(response.isSuccessful) {
            adapter.addData(response.body())
        }
    }

    override fun onFailure(call: Call<MutableList<GithubPullRequestModel>>, t: Throwable) {
        t.printStackTrace()

        var message = "Máximo de tentativas atingido."

        if (retries < 2) {
            retries++
            message = "Ocorreu um erro, tentando novamente."
            endpoints.getPullRequests(repository.owner.login, repository.name).enqueue(this)
        } else {
            adapter.removeLoadingView()
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}