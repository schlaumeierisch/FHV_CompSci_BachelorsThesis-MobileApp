package at.fhv.mme.bt_dementia_app.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import at.fhv.mme.bt_dementia_app.R
import at.fhv.mme.bt_dementia_app.databinding.ItemActivityBinding
import at.fhv.mme.bt_dementia_app.model.Activity
import at.fhv.mme.bt_dementia_app.utils.CheckBoxUtils

class ActivityListAdapter(
    private val onSetDone: (Activity, Boolean) -> Unit,
    private val onDelete: (Activity) -> Unit,
) : ListAdapter<Activity, ActivityListAdapter.ViewHolder>(ActivityDiffCallback()) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemActivityBinding.bind(itemView)

        fun databind(activity: Activity, onSetDone: (Activity, Boolean) -> Unit, onDelete: (Activity) -> Unit) {
            val context = binding.root.context

            binding.tvActivityName.text = activity.name
            binding.tvActivityTime.text = context.getString(
                R.string.text_activity_time,
                activity.time.toString()
            )

            binding.ibtnDeleteActivity.setOnClickListener {
                onDelete(activity)
            }

            CheckBoxUtils.setCheckbox(context, binding.cbActivityDone, activity.isDone)
            binding.cbActivityDone.isEnabled = !activity.isDone
            binding.cbActivityDone.setOnClickListener {
                onSetDone(activity, binding.cbActivityDone.isChecked)
            }
        }

        fun unbind() {
            // set onClickListeners null to prevent memory leaks
            binding.cbActivityDone.setOnClickListener(null)
            binding.ibtnDeleteActivity.setOnClickListener(null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(getItem(position), onSetDone, onDelete)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }

    class ActivityDiffCallback : DiffUtil.ItemCallback<Activity>() {
        override fun areItemsTheSame(oldItem: Activity, newItem: Activity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Activity, newItem: Activity): Boolean {
            return oldItem == newItem
        }
    }
}