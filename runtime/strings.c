#include <stdlib.h>
#include <string.h>
#include <stdint.h>

#include "gc.h"
#include "arithmetics.h"

/* Take these from C standard library */
size_t strlen(const char * str);
char * strdup(const char * str);
int strcmp(const char *str1, const char *str2);

char strget(const char * str, size_t idx) {
    return str[idx];
}

int32_t strset(char * str, size_t idx, char chr) {
    str[idx] = chr;
    return 0;
}

char * strsub(const char * str, size_t offset, size_t length) {
    char * res = (char *) malloc((length + 1) * sizeof(char));
    memcpy(res, str + offset, length);
    res[length] = '\0';
    return res;
}

/* `strcat` from C standard library has different semantics */
char * _strcat(const char * str1, const char * str2) {
    size_t len1 = strlen(str1);
    size_t len2 = strlen(str2);
    size_t len = len1 + len2;
    char * res = (char *) malloc((len + 1) * sizeof(char));
    memcpy(res, str1, len1);
    memcpy(res + len1, str2, len2);
    res[len] = '\0';
    return res;
}

char * strmake(size_t length, char chr) {
    char * res = (char *) malloc((length + 1) * sizeof(char));
    memset(res, chr, length);
    res[length] = '\0';
    return res;
}

size_t _strlen_wrapper(const struct count_ptr * p_str) {
    size_t len = strlen(_get_as_string(p_str));
    return to_marked(len);
}

struct count_ptr * _strdup_raw(const char * str) {
    return _make_count_ptr(strdup(str), STRING_TAG);
}

struct count_ptr * _strdup_wrapper(const struct count_ptr * p_str) {
    return _strdup_raw(_get_as_string(p_str));
}

int32_t _strcmp_wrapper(const struct count_ptr * p_str1, const struct count_ptr * p_str2) {
    int32_t res = strcmp(_get_as_string(p_str1), _get_as_string(p_str2));
    return to_marked(res);
}

int32_t _strget_wrapper(const struct count_ptr * p_str, size_t idx) {
    int res = strget(_get_as_string(p_str), from_marked(idx));
    return to_marked(res);
}

int32_t _strset_wrapper(struct count_ptr * p_str, size_t idx, int32_t chr) {
    return strset(_get_as_string(p_str), from_marked(idx), from_marked(chr));
}

struct count_ptr * _strsub_wrapper(const struct count_ptr * p_str, size_t offset, size_t length) {
    char * str = strsub(_get_as_string(p_str), from_marked(offset), from_marked(length));
    return _make_count_ptr(str, STRING_TAG);
}

struct count_ptr * _strcat_wrapper(const struct count_ptr * p_str1, const struct count_ptr * p_str2) {
    char * str = _strcat(_get_as_string(p_str1), _get_as_string(p_str2));
    return _make_count_ptr(str, STRING_TAG);
}

struct count_ptr * _strmake_wrapper(size_t length, int32_t chr) {
    char * str = strmake(from_marked(length), from_marked(chr));
    return _make_count_ptr(str, STRING_TAG);
}
