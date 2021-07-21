package com.gatmauel.admin.service.food;

import com.gatmauel.admin.dto.food.FoodDTO;
import com.gatmauel.admin.entity.category.Category;
import com.gatmauel.admin.entity.food.Food;
import com.gatmauel.admin.repository.category.CategoryRepository;
import com.gatmauel.admin.repository.food.FoodRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class FoodServiceImpl implements FoodService {
    private final CategoryRepository categoryRepository;

    private final FoodRepository foodRepository;

    @Transactional
    @Override
    public FoodDTO register(FoodDTO dto) {
        Category category=categoryRepository.getById(dto.getCategoryId());

        Food food=dtoToEntity(dto);
        log.debug("food : "+food);

        category.addFood(food);
        categoryRepository.save(category);

        List<Food> foodList=category.getFoodList();
        //log.debug("foodList1 : "+foodList); 왜인지 모르겠지만 여기서 출력하면 중복해서 들어있다. 결과적으로는 정상처리 되지만...

        return entityToDTO(foodList.get(foodList.size()-1));
    }

    @Override
    public List<FoodDTO> getList(){
        List<Food> foodList=foodRepository.findByOrderByPriorAsc();

        List<FoodDTO> dtoList=foodList.stream().map(f->entityToDTO(f)).collect(Collectors.toList());

        return dtoList;
    }

    @Transactional
    @Override
    public Map<String, Object> modify(Long id, FoodDTO dto){
        log.debug(dto.getCategoryId());
        Category category=categoryRepository.getById(dto.getCategoryId());

        List<Food> foodList=category.getFoodList();
        log.debug("foodList1 : "+foodList);
        foodList.stream().map(f->{
            if(f.getId()==id){
                f.changeName(dto.getName());
                f.changeImg(dto.getImg());
                f.changePrice(dto.getPrice());
                f.changePrior(dto.getPrior());
                f.changeComp(dto.getComp());
                f.changeDeli(dto.isDeli());
            }

            return f;
        }).collect(Collectors.toList());
        categoryRepository.save(category);
        log.debug("foodList2 : "+foodList);

        Map<String, Object> result=new HashMap<>();
        result.put("updated", id);
        result.put("name", dto.getName());
        result.put("img", dto.getImg());
        result.put("price", dto.getPrice());
        result.put("comp", dto.getComp());
        result.put("prior", dto.getPrior());
        result.put("deli", dto.isDeli());

        return result;
    }

    @Transactional
    @Override
    public Map<String, Long> remove(Long id){
        Food food=foodRepository.getById(id);

        Category category=categoryRepository.getById(food.getCategory().getId());
        category.removeFood(food);

        categoryRepository.save(category);

        Map<String, Long> result=new HashMap<>();
        result.put("deleted", id);

        return result;
    }
}
