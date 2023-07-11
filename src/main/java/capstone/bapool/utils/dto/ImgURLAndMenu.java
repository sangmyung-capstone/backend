package capstone.bapool.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ImgURLAndMenu {

    private String imgUrl;
    private List<Menu> menus;
}
