package at.fhv.mme.bt_dementia_app.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import at.fhv.mme.bt_dementia_app.R
import at.fhv.mme.bt_dementia_app.databinding.ItemMedicationBinding
import at.fhv.mme.bt_dementia_app.model.Medication
import java.util.Locale

class MedicationListAdapter(
    private val onDelete: (Medication) -> Unit
) : ListAdapter<Medication, MedicationListAdapter.ViewHolder>(MedicationDiffCallback()) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemMedicationBinding.bind(itemView)

        fun databind(medication: Medication, onDelete: (Medication) -> Unit) {
            val context = binding.root.context

            binding.tvMedicationName.text = medication.name
            binding.tvMedicationTime.text = context.getString(
                R.string.text_medication_day_time,
                medication.dayOfWeek.name.lowercase(Locale.ROOT)
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                medication.time
            )
            binding.tvMedicationAmount.text = context.getString(
                R.string.text_medication_amount,
                medication.amount.toString(),
                medication.unit
            )

            binding.ibtnDeleteMedication.setOnClickListener {
                onDelete(medication)
            }
        }

        fun unbind() {
            // set onClickListener null to prevent memory leaks
            binding.ibtnDeleteMedication.setOnClickListener(null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_medication, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(getItem(position), onDelete)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }

    class MedicationDiffCallback : DiffUtil.ItemCallback<Medication>() {
        override fun areItemsTheSame(oldItem: Medication, newItem: Medication): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Medication, newItem: Medication): Boolean {
            return oldItem == newItem
        }
    }
}