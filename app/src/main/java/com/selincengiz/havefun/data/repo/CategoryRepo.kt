package com.selincengiz.havefun.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.common.Resource
import com.selincengiz.havefun.data.model.Category
import com.selincengiz.havefun.data.model.Event

class CategoryRepo(private val db: FirebaseFirestore) {

    fun fireBaseCategoryLiveRead(result: (Resource<List<Category>>) -> Unit) {
        try {
            db.collection("categories").addSnapshotListener { snapshot, error ->

                val tempList = arrayListOf<Category>()

                snapshot?.forEach { document ->

                    tempList.add(

                        Category(
                            document.get("id") as String?,
                            document.get("url") as String?,
                            document.get("text") as String?,
                        )
                    )

                }
                error?.let {
                    result(Resource.Error(it))
                } ?: kotlin.run {
                    result(Resource.Success(tempList))
                }


            }
        } catch (e: Exception) {
            result(Resource.Error(e))
        }

    }

    fun categoryFirebase(
        category: String?,
        success: (String?) -> Unit,
        fail: () -> Unit,
        error: (Exception) -> Unit
    ) {
        try {
            db.collection("categories").get().addOnSuccessListener { documents ->
                var isThere = false
                for (document in documents) {
                    val txt = document.get("text") as String?
                    val src = document.get("url") as String?

                    if (txt.equals(category)) {
                        success(src)
                        isThere = true
                    }


                }

                if (!isThere) {
                    fail()
                }
            }
        } catch (e: Exception) {
            error(e)
        }


    }


}