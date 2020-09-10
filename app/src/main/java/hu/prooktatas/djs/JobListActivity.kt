package hu.prooktatas.djs

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.prooktatas.djs.adapter.JobItemClickHandler
import hu.prooktatas.djs.adapter.JobListAdapter
import hu.prooktatas.djs.model.JobSearchResult

class JobListActivity : AppCompatActivity(), JobItemClickHandler {


    private lateinit var jobListAdapter: JobListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var noResultsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_list)

        noResultsTextView = findViewById(R.id.tvNoResults)

        recyclerView = findViewById(R.id.jobResultRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val items = intent.getSerializableExtra(EXTRA_PARAM_SEARCH_RESULTS) as? List<JobSearchResult>
        jobListAdapter = items?.let {
            JobListAdapter(items, this)
        } ?: JobListAdapter(emptyList(), this)

        if (items == null || items.isEmpty()) {
            noResultsTextView.visibility = View.VISIBLE
        } else {
            noResultsTextView.visibility = View.GONE
        }

        recyclerView.adapter = jobListAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

    }

    override fun itemClicked(item: JobSearchResult) {
        openWebPage(item.url)
    }

    private fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }



}

const val EXTRA_PARAM_SEARCH_RESULTS = "searchResults"