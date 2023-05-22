(function (root, factory) {
  if (typeof define === 'function' && define.amd)
    define(['exports', './kotlin-kotlin-stdlib-js-ir.js'], factory);
  else if (typeof exports === 'object')
    factory(module.exports, require('./kotlin-kotlin-stdlib-js-ir.js'));
  else {
    if (typeof this['kotlin-kotlin-stdlib-js-ir'] === 'undefined') {
      throw new Error("Error loading module 'kotlin-wrappers-kotlin-react-core-js-ir'. Its dependency 'kotlin-kotlin-stdlib-js-ir' was not found. Please, check whether 'kotlin-kotlin-stdlib-js-ir' is loaded prior to 'kotlin-wrappers-kotlin-react-core-js-ir'.");
    }
    root['kotlin-wrappers-kotlin-react-core-js-ir'] = factory(typeof this['kotlin-wrappers-kotlin-react-core-js-ir'] === 'undefined' ? {} : this['kotlin-wrappers-kotlin-react-core-js-ir'], this['kotlin-kotlin-stdlib-js-ir']);
  }
}(this, function (_, kotlin_kotlin) {
  'use strict';
  //region block: imports
  var Unit_getInstance = kotlin_kotlin.$_$.b4;
  var classMeta = kotlin_kotlin.$_$.k7;
  var setMetadataFor = kotlin_kotlin.$_$.i8;
  //endregion
  //region block: pre-declaration
  setMetadataFor(StateInstance, 'StateInstance', classMeta, undefined, undefined, undefined, undefined, []);
  setMetadataFor(StateSetter, 'StateSetter', classMeta, undefined, undefined, undefined, undefined, []);
  //endregion
  function IntrinsicType(tagName) {
    var tmp$ret$1;
    // Inline function 'kotlin.js.unsafeCast' call
    var tmp$ret$0;
    // Inline function 'kotlin.js.asDynamic' call
    tmp$ret$0 = tagName;
    tmp$ret$1 = tmp$ret$0;
    return tmp$ret$1;
  }
  function ReactNode(source) {
    var tmp$ret$1;
    // Inline function 'kotlin.js.unsafeCast' call
    var tmp$ret$0;
    // Inline function 'kotlin.js.asDynamic' call
    tmp$ret$0 = source;
    tmp$ret$1 = tmp$ret$0;
    return tmp$ret$1;
  }
  function StateInstance() {
  }
  StateInstance.prototype.component1_7eebsc_k$ = function () {
    var tmp$ret$0;
    // Inline function 'kotlin.js.asDynamic' call
    tmp$ret$0 = this;
    return tmp$ret$0[0];
  };
  StateInstance.prototype.component2_7eebsb_k$ = function () {
    var tmp$ret$0;
    // Inline function 'kotlin.js.asDynamic' call
    tmp$ret$0 = this;
    return tmp$ret$0[1];
  };
  StateInstance.prototype.getValue_elrtsm_k$ = function (thisRef, property) {
    var tmp$ret$0;
    // Inline function 'kotlin.js.asDynamic' call
    tmp$ret$0 = this;
    return tmp$ret$0[0];
  };
  StateInstance.prototype.setValue_r8gule_k$ = function (thisRef, property, value) {
    var tmp$ret$0;
    // Inline function 'kotlin.js.asDynamic' call
    tmp$ret$0 = this;
    tmp$ret$0[1](value);
  };
  function StateSetter() {
  }
  StateSetter.prototype.invoke_n4yk90_k$ = function (value) {
    var tmp$ret$0;
    // Inline function 'kotlin.js.asDynamic' call
    tmp$ret$0 = this;
    tmp$ret$0(value);
  };
  StateSetter.prototype.invoke_bmpgyi_k$ = function (transform) {
    var tmp$ret$0;
    // Inline function 'kotlin.js.asDynamic' call
    tmp$ret$0 = this;
    tmp$ret$0(transform);
  };
  function set_displayName(_this__u8e3s4, value) {
    var tmp$ret$0;
    // Inline function 'kotlin.js.asDynamic' call
    tmp$ret$0 = _this__u8e3s4;
    tmp$ret$0.displayName = value;
  }
  function get_displayName(_this__u8e3s4) {
    var tmp$ret$0;
    // Inline function 'kotlin.js.asDynamic' call
    tmp$ret$0 = _this__u8e3s4;
    return tmp$ret$0.displayName;
  }
  return _;
}));
