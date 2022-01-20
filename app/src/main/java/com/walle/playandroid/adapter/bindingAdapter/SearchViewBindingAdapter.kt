package com.walle.playandroid.adapter.bindingAdapter

import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

@BindingAdapter("onTextSubmit","onTextChange", requireAll = false)
fun setOnTextChangeListener(searchView: SearchView, onTextSubmit: (String) -> Unit, onTextChange: (String) -> Unit) {
    searchView.setOnQueryTextListener(object :
        SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let {
                onTextSubmit.invoke(query)
            }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            newText?.let {
                onTextChange.invoke(newText)
            }
            return false
        }

    })
}

@BindingAdapter("queryText")
fun setQueryText(searchView: SearchView,queryText:String) {
    if (searchView.query.toString()!=queryText) {
        searchView.setQuery(queryText,false)
    }
}

@InverseBindingAdapter(
    attribute = "queryText",
    event = "queryTextFocusAttrChanged")
fun getQueryText(searchView: SearchView) =searchView.query.toString()

@BindingAdapter("queryTextFocusAttrChanged")
fun setListeners(
    searchView: SearchView,
    attrChange: InverseBindingListener
) {
    // Set a listener for click, focus, touch, etc.
    searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
        attrChange.onChange()
    }
}
