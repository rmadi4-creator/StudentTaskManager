# Student Task Manager | نظام إدارة المهام للطلاب

تطبيق Android بسيط مبني بلغة **Kotlin** يستخدم **Room Database** لمساعدة الطالب على تنظيم مهامه الدراسية وربطها بالمواد الجامعية، مع متابعة حالة الإنجاز لكل مهمة.

> تكليف جامعي – مقرر متعلق بتطوير تطبيقات الأندرويد وقواعد البيانات (Room).

---

## نظرة عامة

| | |
|---|---|
| **اسم المشروع** | Student Task Manager |
| **اللغة** | Kotlin |
| **قاعدة البيانات** | Room (SQLite) |
| **المعمارية** | MVVM (Entity → DAO → Database → Repository → ViewModel → UI) |
| **Min SDK** | 24 (Android 7.0) |
| **Target SDK** | 34 |

##المزايا

- إضافة / تعديل / حذف مواد دراسية (Subjects).
- إضافة / تعديل / حذف مهام (Tasks) وربط كل مهمة بمادة دراسية.
- تحديد تاريخ استحقاق لكل مهمة عبر DatePicker.
- تحديد حالة الإنجاز (منجزة / غير منجزة) مباشرة من القائمة.
- حذف مادة دراسية يحذف تلقائيًا كل مهامها (Foreign Key + CASCADE).
- تخزين دائم للبيانات محليًا عبر Room.

## 🗂️ هيكل قاعدة البيانات

**Subjects** (One) → **Tasks** (Many) — علاقة One-to-Many عبر `subjectId` كـ Foreign Key.

راجع ملف [`docs/ERD.dbml`](docs/ERD.dbml) لعرض الـ ERD كاملاً على [dbdiagram.io](https://dbdiagram.io).

## 🏗️ هيكل المشروع

```
app/src/main/java/com/example/studenttaskmanager/
├── data/
│   ├── local/
│   │   ├── entity/
│   │   │   ├── Subject.kt          # Entity المادة الدراسية
│   │   │   └── Task.kt             # Entity المهمة
│   │   ├── relation/
│   │   │   └── TaskWithSubject.kt  # عرض مهمة مع بيانات مادتها
│   │   ├── dao/
│   │   │   ├── SubjectDao.kt
│   │   │   └── TaskDao.kt
│   │   └── AppDatabase.kt          # @Database + Singleton
│   └── repository/
│       ├── SubjectRepository.kt
│       └── TaskRepository.kt
├── viewmodel/
│   ├── SubjectViewModel.kt
│   └── TaskViewModel.kt
└── ui/
    ├── MainActivity.kt             # الشاشة الرئيسية (قائمة المهام)
    ├── AddEditTaskActivity.kt      # إضافة / تعديل مهمة
    ├── TaskDetailsActivity.kt      # تفاصيل المهمة
    ├── SubjectActivity.kt          # إدارة المواد الدراسية
    └── adapter/
        ├── TaskAdapter.kt
        └── SubjectAdapter.kt
```

## ⚙️ المتطلبات

- Android Studio (Hedgehog أو أحدث).
- JDK 17.
- Android SDK 34.

### أخطاء Room الشائعة وحلولها

| الخطأ | السبب | الحل |
|---|---|---|
| `Cannot find implementation for AppDatabase` | نسيان معالج KSP | تأكد من وجود `id("com.google.devtools.ksp")` في `build.gradle.kts` |
| `Room cannot verify the data integrity` | تغيير بنية الجدول بدون Migration | استخدم `fallbackToDestructiveMigration()` (مستخدم بالفعل) أو أضف Migration رسمية |
| `FOREIGN KEY constraint failed` | محاولة إضافة Task بـ subjectId غير موجود | تأكد من إضافة مادة دراسية واحدة على الأقل قبل إضافة مهمة |
| `IllegalStateException: Cannot access database on the main thread` | استدعاء DAO مباشرة بدون Coroutine | استخدم `viewModelScope.launch` أو `suspend fun` (مطبق بالفعل في المشروع) |app/src`، `docs/ERD.dbml`، و `README.md`).
