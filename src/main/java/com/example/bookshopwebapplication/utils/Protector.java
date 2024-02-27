package com.example.bookshopwebapplication.utils;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Protector<R> {
    private R returnObject;
    private Throwable exceptionObject;

    // Nhận vào hàm có trả về giá trị: insert(t), getByXXX(t), getAll()
    public static <R> Protector<R> of(Supplier<R> f) {
        Protector<R> p = new Protector<>();
        try {
            p.returnObject = f.get();
        } catch (Exception e) {
            System.err.println(e.toString());
            p.exceptionObject = e;
        }
        return p;
    }

    // Nhận vào hàm không trả về giá trị: update(t), delete(t)
    public static Protector<Boolean> of(Runnable f) {
        Protector<Boolean> p = new Protector<>();
        try {
            f.run();
            p.returnObject = false;
        } catch (Exception e) {
            System.err.println(e.toString());
            p.exceptionObject = e;
        }
        return p;
    }

    // Lấy ra đối tượng trả về
    public Optional<R> get() {
        return Optional.ofNullable(returnObject);
    }

    // Lấy ra đối tượng trả về với kiểu của chính đối tượng đó
    // Nhưng buộc phải nhận vào một giá trị mặc định
    public R get(R r) {
        return Optional.ofNullable(returnObject).orElse(r);
    }

    // Lấy ra đối tượng trả về với kiểu của chính đối tượng đó
    // Nhưng buộc phải nhận vào một hàm thực thi mặc định
    public R get(Supplier<R> f) {
        return Optional.ofNullable(returnObject).orElseGet(f);
    }

    // Gọi callback khi thực hiện (các) hàm thành công
    public Protector<R> done(Consumer<R> callback) {
        Optional.ofNullable(returnObject).ifPresent(callback);
        return this;
    }

    // Gọi callback khi thực hiện (các) hàm thất bại (xuất hiện ngoại lệ)
    public Protector<R> fail(Consumer<Throwable> callback) {
        Optional.ofNullable(exceptionObject).ifPresent(callback);
        return this;
    }

    @Override
    public String toString() {
        return String.format("Protector: returnObject: %s -- exceptionObject: %s", returnObject, exceptionObject);
    }
}