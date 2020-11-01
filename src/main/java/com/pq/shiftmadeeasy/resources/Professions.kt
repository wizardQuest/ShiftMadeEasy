package com.pq.shiftmadeeasy.resources

import android.os.Parcelable
import com.pq.shiftmadeeasy.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfessionsStructure(var professionIcon: Int, var professionName: String) : Parcelable

class Professions {
    companion object {
        fun getAllProfessions(): List<ProfessionsStructure> {
            return mutableListOf(
                ProfessionsStructure(R.drawable.ic_user_girl, "Air Traffic Controller"),
                ProfessionsStructure(R.drawable.ic_user_boy, "Pilot"),
                ProfessionsStructure(R.drawable.ic_user_boy, "Security Guard"),
                ProfessionsStructure(R.drawable.ic_user_boy, "Police"),
                ProfessionsStructure(R.drawable.ic_user_girl, "Nurse"),
                ProfessionsStructure(R.drawable.ic_user_boy, "Doctor")
            )
        }
    }
}