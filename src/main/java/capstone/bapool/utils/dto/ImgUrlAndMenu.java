package capstone.bapool.utils.dto;

import capstone.bapool.restaurant.dto.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ImgUrlAndMenu {

    private String imgUrl;
    private List<Menu> menus;
}
