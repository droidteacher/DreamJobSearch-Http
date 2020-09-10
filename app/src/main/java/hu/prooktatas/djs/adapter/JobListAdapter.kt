package hu.prooktatas.djs.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.prooktatas.djs.R
import hu.prooktatas.djs.model.JobSearchResult

interface JobItemClickHandler {
    fun itemClicked(item: JobSearchResult)
}

class JobListAdapter(val searchResults: List<JobSearchResult>, private val clickHandler: JobItemClickHandler): RecyclerView.Adapter<JobItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobItemViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.job_item_row, parent, false)
        return JobItemViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: JobItemViewHolder, position: Int) {
        val currentJob = searchResults[position]
        holder.titleTextView.text = currentJob.title
        holder.companyTextView.text = currentJob.company
        holder.locationTextView.text = currentJob.location

        holder.itemView.setOnClickListener {
            clickHandler.itemClicked(currentJob)
        }
    }

    override fun getItemCount(): Int {
        return searchResults.size
    }
}

class JobItemViewHolder(v: View): RecyclerView.ViewHolder(v) {

    val titleTextView: TextView = v.findViewById(R.id.tvJobTitle)
    val companyTextView: TextView = v.findViewById(R.id.tvCompany)
    val locationTextView: TextView = v.findViewById(R.id.tvLocation)

}