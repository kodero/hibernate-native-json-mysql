/**
 * Copyright (C) 2016 Marvin Herman Froeder (marvin@marvinformatics.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.corvid.json.hibernate.usertype;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.MapPath;

public class JsonExpressions {

    public static <E extends Comparable<E>> ComparableExpression<E> jsonText(Path<?> path, Path<E> property) {
        return Expressions.comparableTemplate(property.getType(), "json_text({0}, {1})", path, property.getMetadata().getName());
    }

    public static <E extends Comparable<E>> ComparableExpression<E> jsonText(MapPath<?, E, ?> path, String property) {
        return Expressions.comparableTemplate(path.getValueType(), "json_text({0}, {1})", path, property);
    }
}