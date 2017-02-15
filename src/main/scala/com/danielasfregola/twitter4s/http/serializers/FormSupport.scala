package com.danielasfregola.twitter4s.http.serializers

import shapeless._
import labelled.{FieldType, field}

// copied and adapted from http://stackoverflow.com/a/31641779
private[twitter4s] trait FormSupport {

  trait FromMap[L <: HList] {
    def apply(m: Map[String, Any]): Option[L]
  }

  trait LowPriorityFromMap {
    implicit def hconsFromMap1[K <: Symbol, V, T <: HList](implicit witness: Witness.Aux[K],
                                                           typeable: Typeable[V],
                                                           fromMapT: Lazy[FromMap[T]]): FromMap[FieldType[K, V] :: T] =
      new FromMap[FieldType[K, V] :: T] {
        def apply(m: Map[String, Any]): Option[FieldType[K, V] :: T] =
          for {
            v <- m.get(witness.value.name)
            h <- typeable.cast(v)
            t <- fromMapT.value(m)
          } yield field[K](h) :: t
      }
  }

  object FromMap extends LowPriorityFromMap {
    implicit val hnilFromMap: FromMap[HNil] = new FromMap[HNil] {
      def apply(m: Map[String, Any]): Option[HNil] = Some(HNil)
    }

    implicit def hconsFromMap0[K <: Symbol, V, R <: HList, T <: HList](implicit
                                                                       witness: Witness.Aux[K],
                                                                       gen: LabelledGeneric.Aux[V, R],
                                                                       fromMapH: FromMap[R],
                                                                       fromMapT: FromMap[T]): FromMap[FieldType[K, V] :: T] =
      new FromMap[FieldType[K, V] :: T] {
        def apply(m: Map[String, Any]): Option[FieldType[K, V] :: T] = for {
          v <- m.get(witness.value.name)
          r <- Typeable[Map[String, Any]].cast(v)
          h <- fromMapH(r)
          t <- fromMapT(m)
        } yield field[K](gen.from(h)) :: t
      }
  }

  class ConvertHelper[A] {
    def from[R <: HList](s: String)(implicit
                                    gen: LabelledGeneric.Aux[A, R],
                                    fromMap: FromMap[R]): Option[A] = {
      val m: Map[String, String] = s.split("&").map{ m =>
        val t = m.split("=", 2)
        t.head -> t.tail.mkString
      }.toMap
      from(m)
    }


    def from[R <: HList](m: Map[String, Any])(implicit
                                              gen: LabelledGeneric.Aux[A, R],
                                              fromMap: FromMap[R]): Option[A] =
      fromMap(m).map(gen.from)
  }

  def to[A]: ConvertHelper[A] = new ConvertHelper[A]

}
