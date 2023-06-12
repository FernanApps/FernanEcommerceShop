package pe.fernanapps.shop.domain.model.product

import pe.fernanapps.shop.domain.model.To

data class Product(
    override val id: String,
    val title: String,
    val subtitle: String?,
    val description: String?,
    val price: Float,
    val size: ArrayList<String>,
    val image: String,
    val category: String,
    val favorite: Boolean = false
): java.io.Serializable, To {
    override fun toString(): String {
        return "Product(id='$id', title='$title', subtitle=$subtitle, description=$description, price=$price, size=$size, image='$image', category='$category', favorite=$favorite)"
    }
}

/*
    id
    sku
    name
    price
    weight
    descriptions
    thumbnail
    image
    category
    create_date_
    stock


XS,S,M,L,XL,XXL

Tienes que generarme productos de cada categoria, pero que sea aleatorio el tamaño entre 10 a 20 productos,
por ejemplo la cart1 puede tener 13 productos, la cat10 puede tener 20, el tamaño de productos por categoria
tu lo elige, en cuanto a los atributos del producto tiene que tener:
id, title, subtitle, descriptions, price (en Float), size ( que sea un array, de los disponibles,
 cualquiera de estos XS,S,M,L,XL,XXL), category (el id de categoria. ejemplo cat12), y image (la imagen tiene
 ser uno que exista en la web)
    {
		"id": "id1",
		"title": "The Marc Jacobs",
		"subtitle": "Traveler Tote",
		"description" : "",
		"price": 195.0,
		"size" :
		"category" : "cat15",
		"image": "https://www.cuyana.com/dw/image/v2/BDQQ_PRD/on/demandware.static/-/Sites-master-catalog-cuyana/default/dw60696187/images/2023_01Jan/easy-tote-dark-coral/PLP_Hero_1080x1350_SP23_EasyTote_DarkCoral_Hero_2785.jpg?sw=768&sh=1152"
	},

 */











