package com.example.tabletalk.utils

import android.widget.CheckBox
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.LiveData
import com.example.android_application_course.R
import com.example.android_application_course.views.CustomTextInput
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("android:text")
fun setText(customTextInput: CustomTextInput, text: String) {
    if (customTextInput.text != text) {
        customTextInput.text = text
    }
}

@BindingAdapter("android:text")
fun setText(customTextInput: CustomTextInput, text: LiveData<String>) {
    if (customTextInput.text != text.value) {
        customTextInput.text = text.value ?: ""
    }
}

@InverseBindingAdapter(attribute = "android:text")
fun getText(customTextInput: CustomTextInput): String {
    return customTextInput.text
}

@BindingAdapter("android:textAttrChanged")
fun setTextWatcher(customTextInput: CustomTextInput, textAttrChanged: InverseBindingListener) {
    customTextInput.findViewById<TextInputEditText>(R.id.text_input_edit_text)
        .doOnTextChanged { _, _, _, _ ->
            textAttrChanged.onChange()
        }
}

@BindingAdapter("helperTextEnabled")
fun setHelperTextEnabled(customTextInput: CustomTextInput, enabled: Boolean) {
    customTextInput.helperTextEnabled = enabled
}

@InverseBindingAdapter(attribute = "helperTextEnabled")
fun getHelperTextEnabled(customTextInput: CustomTextInput): Boolean {
    return customTextInput.helperTextEnabled
}

@BindingAdapter("helperTextEnabledAttrChanged")
fun setHelperTextEnabledListener(customTextInput: CustomTextInput, attrChange: InverseBindingListener) {
    customTextInput.findViewById<TextInputEditText>(R.id.text_input_edit_text)
        .doOnTextChanged { _, _, _, _ ->
            attrChange.onChange()
        }
}

@BindingAdapter("helperText")
fun setHelperText(customTextInput: CustomTextInput, helperText: String?) {
    customTextInput.helperText = helperText ?: ""
}

@InverseBindingAdapter(attribute = "helperText")
fun getHelperText(customTextInput: CustomTextInput): String? {
    return customTextInput.helperText
}

@BindingAdapter("helperTextAttrChanged")
fun setHelperTextWatcher(customTextInput: CustomTextInput, helperTextAttrChanged: InverseBindingListener) {
    customTextInput.findViewById<TextInputEditText>(R.id.text_input_edit_text)
        .doOnTextChanged { _, _, _, _ ->
            helperTextAttrChanged.onChange()
        }
}

@BindingAdapter("android:text")
fun setText(editText: EditText, text: String) {
    if (editText.text.toString() != text) {
        editText.setText(text)
    }
}

@BindingAdapter("android:text")
fun setText(editText: EditText, text: LiveData<String>) {
    if (editText.text.toString() != text.value) {
        editText.setText(text.value ?: "")
    }
}

@InverseBindingAdapter(attribute = "android:text")
fun getText(editText: EditText): String {
    return editText.text.toString()
}

@BindingAdapter("android:checked")
fun setChecked(checkBox: CheckBox, checked: Boolean) {
    if (checkBox.isChecked != checked) {
        checkBox.isChecked = checked
    }
}

@BindingAdapter("android:checked")
fun setChecked(checkBox: CheckBox, checked: LiveData<Boolean>) {
    if (checkBox.isChecked != checked.value) {
        checkBox.isChecked = checked.value ?: false
    }
}

@InverseBindingAdapter(attribute = "android:checked")
fun getChecked(checkBox: CheckBox): Boolean {
    return checkBox.isChecked
}