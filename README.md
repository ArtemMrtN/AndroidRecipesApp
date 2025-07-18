Учебное Android-приложение-справочник: ≈ 40 рецептов, 6 категорий, избранное и
«умный» слайдер порций, который автоматически перерасчитывает количество
ингредиентов. Разработка: **ноябрь 2024 — январь 2025**.

---

## 📋 Описание проекта

* **Оффлайн-доступ** — все данные хранятся локально в Room; приложение
  открывается и работает без интернета.  
* **Избранное** — свайп в карточке добавляет рецепт в закладки, состояние
  синхронно отражается в списке.  
* **Слайдер порций** — пользователь задаёт 1–10 порций, ингредиенты
  пересчитываются «на лету».  
* **Чистая архитектура MVVM** и DI через Hilt — логика отделена от UI, поэтому
  проект легко расширять.

---

## 🚀 Установка и запуск

1. Клонируйте репозиторий
git clone [https://github.com/ArtemMrtN/AndroidRecipesApp/](https://github.com/ArtemMrtN/AndroidRecipesApp/)
cd AndroidRecipesApp

2. Соберите и установите APK на подключённое устройство / эмулятор
./gradlew installDebug

---

## Контакты
Автор — Мартынов Артем
✉️ artemmrt@gmail.com · 🖇 [LinkedIn](https://www.linkedin.com/in/artem-n-martynov/)
