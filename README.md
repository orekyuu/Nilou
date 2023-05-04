# Nilou
NilouはJava17以上のWebアプリケーション向けのリバースルーター生成するライブラリです

- Annotation Processorを使用して、リバースルーターを生成します
- jakarta.ws.rs / javax.ws.rsのアノテーションを処理してハンドラーメソッド毎のUriBuilderを提供します
- PathVariableやNotNullなQueryParameterのような必須パラメータは組み立ての最初に要求されるため、漏れが発生しません
- 実行時にはNilou以外のライブラリへの依存がありません

## 動作環境
| ライブラリのバージョン | 要求Javaバージョン | jakarta eeサポート | java eeサポート | spring mvcサポート |
|---|---|:---:|:---:|:---:|
| 0.0.3 〜 | 17 | o | o | x|

## 例
JAX-RSを利用する場合、`@UriBuilder`をControllerに宣言します
### コード例
```test.HogeController.java
package test;
import net.orekyuu.nilou.UriBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/hoge")
@UriBuilder
public class HogeController {

  @Path("")
  @GET
  public void all() {}

  @Path("{id}")
  @GET
  public void get(@PathParam("id") String id) {}
}
```
```test.FugaController.java
package test;
import net.orekyuu.nilou.UriBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/fuga")
@UriBuilder
public class FugaController {

  @Path("")
  @GET
  public void all() {}
}
```
### 生成されるリバースルーター
```net.orekyuu.nilou.Endpoints.java
package net.orekyuu.nilou;

import java.lang.String;
import java.util.List;

public final class Endpoints {
    public static final class FugaController {
        public static EndpointUriBuilder all() {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("fuga"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            return builder;
        }
    }

    public static final class HogeController {
        public static EndpointUriBuilder all() {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("hoge"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            return builder;
        }

        public static EndpointUriBuilder get(String id) {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("hoge"), PathSegment.ofVariable("{id}", "id"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            builder.pathParam("id", id);
            return builder;
        }
    }
}
```
### 呼び出し例
```java
Uri hogeGetUri = Endpoints.HogeController
    .get("hogeId")
    .toUri();

Uri fugaAllUri = Endpoints.FugaController
    .all()
    .queryParameter("page", "1")
    .queryParameter("per", "20")
    .toUri();
```

## Setup
```build.gradle
// 生成したコードをIDEから参照できるようにする
sourceSets.main.java.srcDirs += "${buildDir}/generated/sources/annotationProcessor/java/main"

dependencies {
  implementation 'net.orekyuu:nilou-api:0.0.3'
  annotationProcessor 'net.orekyuu:nilou-annotation-processor:0.0.3'
}
```
